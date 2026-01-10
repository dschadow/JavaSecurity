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
package de.dominikschadow.javasecurity.tink.aead;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AesGcmWithAwsKmsSavedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ASSOCIATED_DATA = "Some additional data".getBytes(StandardCharsets.UTF_8);

    @Mock
    private AwsKmsClient awsKmsClient;

    @TempDir
    File tempDir;

    private AesGcmWithAwsKmsSavedKey aes;
    private KeysetHandle testKeysetHandle;

    @BeforeAll
    static void initTink() throws GeneralSecurityException {
        AeadConfig.register();
    }

    @BeforeEach
    void setup() throws Exception {
        aes = new AesGcmWithAwsKmsSavedKey(awsKmsClient);
        testKeysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES128_GCM"));
    }

    @Test
    void constructorInitializesSuccessfully() throws GeneralSecurityException {
        AesGcmWithAwsKmsSavedKey instance = new AesGcmWithAwsKmsSavedKey(awsKmsClient);
        assertNotNull(instance);
    }

    @Test
    void constructorWithNullAwsKmsClientThrowsNoException() throws GeneralSecurityException {
        // The constructor accepts null - validation happens later when using the client
        AesGcmWithAwsKmsSavedKey instance = new AesGcmWithAwsKmsSavedKey(null);
        assertNotNull(instance);
    }

    @Test
    void encryptReturnsEncryptedData() throws Exception {
        byte[] cipherText = aes.encrypt(testKeysetHandle, INITIAL_TEXT, ASSOCIATED_DATA);

        assertNotNull(cipherText);
        assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8));
    }

    @Test
    void encryptWithEmptyAssociatedDataSucceeds() throws Exception {
        byte[] cipherText = aes.encrypt(testKeysetHandle, INITIAL_TEXT, new byte[0]);

        assertNotNull(cipherText);
        assertTrue(cipherText.length > 0);
    }

    @Test
    void decryptReturnsOriginalData() throws Exception {
        byte[] cipherText = aes.encrypt(testKeysetHandle, INITIAL_TEXT, ASSOCIATED_DATA);
        byte[] plainText = aes.decrypt(testKeysetHandle, cipherText, ASSOCIATED_DATA);

        assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8));
    }

    @Test
    void decryptWithWrongAssociatedDataThrowsException() throws Exception {
        byte[] cipherText = aes.encrypt(testKeysetHandle, INITIAL_TEXT, ASSOCIATED_DATA);
        byte[] wrongAssociatedData = "Wrong associated data".getBytes(StandardCharsets.UTF_8);

        assertThrows(GeneralSecurityException.class, () -> 
            aes.decrypt(testKeysetHandle, cipherText, wrongAssociatedData)
        );
    }

    @Test
    void decryptWithCorruptedCipherTextThrowsException() throws Exception {
        byte[] cipherText = aes.encrypt(testKeysetHandle, INITIAL_TEXT, ASSOCIATED_DATA);
        // Corrupt the ciphertext
        cipherText[0] = (byte) (cipherText[0] ^ 0xFF);

        assertThrows(GeneralSecurityException.class, () -> 
            aes.decrypt(testKeysetHandle, cipherText, ASSOCIATED_DATA)
        );
    }

    @Test
    void encryptionAndDecryptionRoundTripIsSuccessful() throws Exception {
        byte[] cipherText = aes.encrypt(testKeysetHandle, INITIAL_TEXT, ASSOCIATED_DATA);
        byte[] plainText = aes.decrypt(testKeysetHandle, cipherText, ASSOCIATED_DATA);

        assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }

    @Test
    void encryptProducesDifferentCipherTextForSameInput() throws Exception {
        byte[] cipherText1 = aes.encrypt(testKeysetHandle, INITIAL_TEXT, ASSOCIATED_DATA);
        byte[] cipherText2 = aes.encrypt(testKeysetHandle, INITIAL_TEXT, ASSOCIATED_DATA);

        // AES-GCM uses random nonces, so encrypting the same plaintext twice should produce different ciphertexts
        assertNotEquals(new String(cipherText1, StandardCharsets.UTF_8), new String(cipherText2, StandardCharsets.UTF_8));
    }

    @Test
    void generateAndStoreKeyDoesNotOverwriteExistingFile() throws Exception {
        File keysetFile = new File(tempDir, "existing-keyset.json");
        assertTrue(keysetFile.createNewFile());
        long originalLength = keysetFile.length();

        aes.generateAndStoreKey(keysetFile);

        // File should remain unchanged (empty) since it already existed
        assertEquals(originalLength, keysetFile.length());
        verify(awsKmsClient, never()).getAead(any());
    }

    @Test
    void generateAndStoreKeyCallsAwsKmsClientForNewFile() throws Exception {
        File keysetFile = new File(tempDir, "new-keyset.json");
        Aead mockAead = mock(Aead.class);
        when(awsKmsClient.getAead(any())).thenReturn(mockAead);
        // Tink internally validates the encrypted keyset, so we need to throw an exception
        // to simulate what happens when AWS KMS is not available, but still verify the call
        when(mockAead.encrypt(any(), any())).thenThrow(new GeneralSecurityException("Mocked AWS KMS encryption"));

        assertFalse(keysetFile.exists());
        
        assertThrows(GeneralSecurityException.class, () -> aes.generateAndStoreKey(keysetFile));

        // Verify that AWS KMS client was called
        verify(awsKmsClient).getAead(contains("aws-kms://"));
        verify(mockAead).encrypt(any(), any());
    }

    @Test
    void loadKeyCallsAwsKmsClient() throws Exception {
        // First create a keyset file using the same mock setup
        File keysetFile = new File(tempDir, "load-test-keyset.json");
        Aead mockAead = mock(Aead.class);
        when(awsKmsClient.getAead(any())).thenReturn(mockAead);
        
        // Mock encrypt to return the plaintext (simulating encryption that returns same bytes)
        when(mockAead.encrypt(any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
        // Mock decrypt to return the ciphertext (simulating decryption that returns same bytes)
        when(mockAead.decrypt(any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
        
        aes.generateAndStoreKey(keysetFile);

        KeysetHandle loadedKey = aes.loadKey(keysetFile);

        assertNotNull(loadedKey);
        // Verify getAead was called twice - once for generate, once for load
        verify(awsKmsClient, times(2)).getAead(contains("aws-kms://"));
    }

    @Test
    void encryptWithNullKeysetHandleThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            aes.encrypt(null, INITIAL_TEXT, ASSOCIATED_DATA)
        );
    }

    @Test
    void decryptWithNullKeysetHandleThrowsException() {
        assertThrows(NullPointerException.class, () -> 
            aes.decrypt(null, INITIAL_TEXT, ASSOCIATED_DATA)
        );
    }
}
