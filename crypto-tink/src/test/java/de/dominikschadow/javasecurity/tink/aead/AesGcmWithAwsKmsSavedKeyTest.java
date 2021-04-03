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

class AesGcmWithAwsKmsSavedKeyTest {
    private static final String AWS_MASTER_KEY_URI = "aws-kms://arn:aws:kms:eu-central-1:776241929911:key/cce9ce6d-526c-44ca-9189-45c54b90f070";
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ASSOCIATED_DATA = "Some additional data".getBytes(StandardCharsets.UTF_8);
    private static final String KEYSET_FILENAME = "src/test/resources/keysets/aead-aes-gcm-kms.json";
    private final File keysetFile = new File(KEYSET_FILENAME);
    private KeysetHandle secretKey;

    private final AesGcmWithAwsKmsSavedKey aes = new AesGcmWithAwsKmsSavedKey();

    @BeforeEach
    protected void setup() throws Exception {
        aes.generateAndStoreKey(keysetFile, AWS_MASTER_KEY_URI);
        secretKey = aes.loadKey(keysetFile, AWS_MASTER_KEY_URI);
    }

    @Test
    @Disabled("This test requires AWS KMS configuration")
    void encryptionAndDecryptionWithValidInputsIsSuccessful() throws Exception {
        byte[] cipherText = aes.encrypt(secretKey, INITIAL_TEXT, ASSOCIATED_DATA);
        byte[] plainText = aes.decrypt(secretKey, cipherText, ASSOCIATED_DATA);

        Assertions.assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }
}