package de.dominikschadow.javasecurity.tink.aead;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AesGcmWithSavedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ASSOCIATED_DATA = "Some additional data".getBytes(StandardCharsets.UTF_8);
    private static final String KEYSET_FILENAME = "keysets/aead-aes-gcm.json";

    private final AesGcmWithSavedKey aesEax = new AesGcmWithSavedKey();
    private KeysetHandle keysetHandle;

    @BeforeEach
    protected void setup() throws Exception {
        keysetHandle = aesEax.loadKey(new File(getClass().getClassLoader().getResource(KEYSET_FILENAME).getFile()));
    }

    @Test
    void encryptionAndDecryptionWithValidInputsIsSuccessful() throws Exception {
        byte[] cipherText = aesEax.encrypt(keysetHandle, INITIAL_TEXT, ASSOCIATED_DATA);
        byte[] plainText = aesEax.decrypt(keysetHandle, cipherText, ASSOCIATED_DATA);

        Assertions.assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }
}