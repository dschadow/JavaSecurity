/*
 * Copyright (C) 2023 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dominikschadow.javasecurity.tink.hybrid;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.hybrid.HybridConfig;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EciesWithAwsKmsSavedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] CONTEXT_INFO = "Some context info".getBytes(StandardCharsets.UTF_8);

    @Mock
    private AwsKmsClient awsKmsClient;

    @TempDir
    File tempDir;

    private EciesWithAwsKmsSavedKey ecies;
    private KeysetHandle testPrivateKeysetHandle;
    private KeysetHandle testPublicKeysetHandle;

    @BeforeAll
    static void initTink() throws GeneralSecurityException {
        HybridConfig.register();
    }

    @BeforeEach
    void setup() throws Exception {
        ecies = new EciesWithAwsKmsSavedKey(awsKmsClient);
        testPrivateKeysetHandle = KeysetHandle.generateNew(KeyTemplates.get("ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM"));
        testPublicKeysetHandle = testPrivateKeysetHandle.getPublicKeysetHandle();
    }

    @Test
    void constructorInitializesSuccessfully() throws GeneralSecurityException {
        EciesWithAwsKmsSavedKey instance = new EciesWithAwsKmsSavedKey(awsKmsClient);
        assertNotNull(instance);
    }

    @Test
    void constructorWithNullAwsKmsClientThrowsNoException() throws GeneralSecurityException {
        // The constructor accepts null - validation happens later when using the client
        EciesWithAwsKmsSavedKey instance = new EciesWithAwsKmsSavedKey(null);
        assertNotNull(instance);
    }

    @Test
    void encryptReturnsEncryptedData() throws Exception {
        byte[] cipherText = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);

        assertNotNull(cipherText);
        assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8));
    }

    @Test
    void encryptWithEmptyContextInfoSucceeds() throws Exception {
        byte[] cipherText = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, new byte[0]);

        assertNotNull(cipherText);
        assertTrue(cipherText.length > 0);
    }

    @Test
    void decryptReturnsOriginalData() throws Exception {
        byte[] cipherText = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);
        byte[] plainText = ecies.decrypt(testPrivateKeysetHandle, cipherText, CONTEXT_INFO);

        assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8));
    }

    @Test
    void decryptWithWrongContextInfoThrowsException() throws Exception {
        byte[] cipherText = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);
        byte[] wrongContextInfo = "Wrong context info".getBytes(StandardCharsets.UTF_8);

        assertThrows(GeneralSecurityException.class, () -> 
            ecies.decrypt(testPrivateKeysetHandle, cipherText, wrongContextInfo)
        );
    }

    @Test
    void decryptWithCorruptedCipherTextThrowsException() throws Exception {
        byte[] cipherText = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);
        // Corrupt the ciphertext
        cipherText[0] = (byte) (cipherText[0] ^ 0xFF);

        assertThrows(GeneralSecurityException.class, () -> 
            ecies.decrypt(testPrivateKeysetHandle, cipherText, CONTEXT_INFO)
        );
    }

    @Test
    void encryptionAndDecryptionRoundTripIsSuccessful() throws Exception {
        byte[] cipherText = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);
        byte[] plainText = ecies.decrypt(testPrivateKeysetHandle, cipherText, CONTEXT_INFO);

        assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }

    @Test
    void encryptProducesDifferentCipherTextForSameInput() throws Exception {
        byte[] cipherText1 = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);
        byte[] cipherText2 = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);

        // ECIES uses random nonces, so encrypting the same plaintext twice should produce different ciphertexts
        assertNotEquals(new String(cipherText1, StandardCharsets.UTF_8), new String(cipherText2, StandardCharsets.UTF_8));
    }

    @Test
    void generateAndStorePrivateKeyDoesNotOverwriteExistingFile() throws Exception {
        File keysetFile = new File(tempDir, "existing-private-keyset.json");
        assertTrue(keysetFile.createNewFile());
        long originalLength = keysetFile.length();

        ecies.generateAndStorePrivateKey(keysetFile);

        // File should remain unchanged (empty) since it already existed
        assertEquals(originalLength, keysetFile.length());
        verify(awsKmsClient, never()).getAead(any());
    }

    @Test
    void generateAndStorePrivateKeyCallsAwsKmsClientForNewFile() throws Exception {
        File keysetFile = new File(tempDir, "new-private-keyset.json");
        Aead mockAead = mock(Aead.class);
        when(awsKmsClient.getAead(any())).thenReturn(mockAead);
        // Tink internally validates the encrypted keyset, so we need to throw an exception
        // to simulate what happens when AWS KMS is not available, but still verify the call
        when(mockAead.encrypt(any(), any())).thenThrow(new GeneralSecurityException("Mocked AWS KMS encryption"));

        assertFalse(keysetFile.exists());
        
        assertThrows(GeneralSecurityException.class, () -> ecies.generateAndStorePrivateKey(keysetFile));

        // Verify that AWS KMS client was called
        verify(awsKmsClient).getAead(contains("aws-kms://"));
        verify(mockAead).encrypt(any(), any());
    }

    @Test
    void generateAndStorePublicKeyDoesNotOverwriteExistingFile() throws Exception {
        File keysetFile = new File(tempDir, "existing-public-keyset.json");
        assertTrue(keysetFile.createNewFile());
        long originalLength = keysetFile.length();

        ecies.generateAndStorePublicKey(testPrivateKeysetHandle, keysetFile);

        // File should remain unchanged (empty) since it already existed
        assertEquals(originalLength, keysetFile.length());
    }

    @Test
    void generateAndStorePublicKeyCreatesNewFile() throws Exception {
        File keysetFile = new File(tempDir, "new-public-keyset.json");
        assertFalse(keysetFile.exists());

        ecies.generateAndStorePublicKey(testPrivateKeysetHandle, keysetFile);

        assertTrue(keysetFile.exists());
        assertTrue(keysetFile.length() > 0);
    }

    @Test
    void loadPrivateKeyCallsAwsKmsClient() throws Exception {
        // First create a keyset file using the same mock setup
        File keysetFile = new File(tempDir, "load-test-private-keyset.json");
        Aead mockAead = mock(Aead.class);
        when(awsKmsClient.getAead(any())).thenReturn(mockAead);
        
        // Mock encrypt to return the plaintext (simulating encryption that returns same bytes)
        when(mockAead.encrypt(any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
        // Mock decrypt to return the ciphertext (simulating decryption that returns same bytes)
        when(mockAead.decrypt(any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
        
        ecies.generateAndStorePrivateKey(keysetFile);

        KeysetHandle loadedKey = ecies.loadPrivateKey(keysetFile);

        assertNotNull(loadedKey);
        // Verify getAead was called twice - once for generate, once for load
        verify(awsKmsClient, times(2)).getAead(contains("aws-kms://"));
    }

    @Test
    void loadPublicKeyReturnsKeysetHandle() throws Exception {
        File keysetFile = new File(tempDir, "load-test-public-keyset.json");
        ecies.generateAndStorePublicKey(testPrivateKeysetHandle, keysetFile);

        KeysetHandle loadedKey = ecies.loadPublicKey(keysetFile);

        assertNotNull(loadedKey);
    }

    @Test
    void encryptWithNullKeysetHandleThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ecies.encrypt(null, INITIAL_TEXT, CONTEXT_INFO)
        );
    }

    @Test
    void decryptWithNullKeysetHandleThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            ecies.decrypt(null, INITIAL_TEXT, CONTEXT_INFO)
        );
    }

    @Test
    void encryptWithPublicKeyAndDecryptWithPrivateKeySucceeds() throws Exception {
        // This test verifies the asymmetric nature of hybrid encryption
        byte[] cipherText = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);
        byte[] plainText = ecies.decrypt(testPrivateKeysetHandle, cipherText, CONTEXT_INFO);

        assertArrayEquals(INITIAL_TEXT, plainText);
    }

    @Test
    void decryptWithPublicKeyThrowsException() throws Exception {
        byte[] cipherText = ecies.encrypt(testPublicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);

        // Decrypting with public key should fail - only private key can decrypt
        assertThrows(GeneralSecurityException.class, () -> 
            ecies.decrypt(testPublicKeysetHandle, cipherText, CONTEXT_INFO)
        );
    }
}
