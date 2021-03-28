package de.dominikschadow.javasecurity.tink.hybrid;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

class EciesWithGeneratedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] CONTEXT_INFO = "Some additional data".getBytes(StandardCharsets.UTF_8);

    private final EciesWithGeneratedKey ecies = new EciesWithGeneratedKey();

    @Test
    void encryptionAndDecryptionWithValidInputsIsSuccessful() throws Exception {
        KeysetHandle privateKeysetHandle = ecies.generatePrivateKey();
        KeysetHandle publicKeysetHandle = ecies.generatePublicKey(privateKeysetHandle);

        byte[] cipherText = ecies.encrypt(publicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);
        byte[] plainText = ecies.decrypt(privateKeysetHandle, cipherText, CONTEXT_INFO);

        Assertions.assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }

    @Test
    void decryptionWithInvalidAssociatedDataFails() throws Exception {
        KeysetHandle privateKeysetHandle = ecies.generatePrivateKey();
        KeysetHandle publicKeysetHandle = ecies.generatePublicKey(privateKeysetHandle);

        byte[] cipherText = ecies.encrypt(publicKeysetHandle, INITIAL_TEXT, CONTEXT_INFO);

        Exception exception = assertThrows(GeneralSecurityException.class, () -> ecies.decrypt(privateKeysetHandle, cipherText, "abc".getBytes(StandardCharsets.UTF_8)));

        assertTrue(exception.getMessage().contains("decryption failed"));
    }
}