package de.dominikschadow.javasecurity.tink.aead;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Disabled("These test require AWS KMS configuration")
class AesGcmWithAwsKmsSavedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ASSOCIATED_DATA = "Some additional data".getBytes(StandardCharsets.UTF_8);
    private static final String KEYSET_FILENAME = "src/test/resources/keysets/aead-aes-gcm-kms.json";
    private final File keysetFile = new File(KEYSET_FILENAME);
    private KeysetHandle secretKey;

    private AesGcmWithAwsKmsSavedKey aes;

    @BeforeEach
    protected void setup() throws Exception {
        aes = new AesGcmWithAwsKmsSavedKey();

        aes.generateAndStoreKey(keysetFile);
        secretKey = aes.loadKey(keysetFile);
    }

    @Test
    void encryptionAndDecryptionWithValidInputsIsSuccessful() throws Exception {
        byte[] cipherText = aes.encrypt(secretKey, INITIAL_TEXT, ASSOCIATED_DATA);
        byte[] plainText = aes.decrypt(secretKey, cipherText, ASSOCIATED_DATA);

        Assertions.assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }
}