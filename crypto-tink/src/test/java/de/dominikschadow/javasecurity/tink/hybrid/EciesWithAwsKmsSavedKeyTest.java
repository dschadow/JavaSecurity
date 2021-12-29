package de.dominikschadow.javasecurity.tink.hybrid;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Disabled("These tests require AWS KMS configuration")
class EciesWithAwsKmsSavedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] CONTEXT_INFO = "Some additional data".getBytes(StandardCharsets.UTF_8);
    private static final String PRIVATE_KEYSET_FILENAME = "src/test/resources/keysets/hybrid-ecies-kms-private.json";
    private static final String PUBLIC_KEYSET_FILENAME = "src/test/resources/keysets/hybrid-ecies-kms-public.json";
    private final File privateKeysetFile = new File(PRIVATE_KEYSET_FILENAME);
    private final File publicKeysetFile = new File(PUBLIC_KEYSET_FILENAME);
    private KeysetHandle publicKey;
    private KeysetHandle privateKey;

    private EciesWithAwsKmsSavedKey ecies;

    @BeforeEach
    protected void setup() throws Exception {
        ecies = new EciesWithAwsKmsSavedKey();

        ecies.generateAndStorePrivateKey(privateKeysetFile);
        privateKey = ecies.loadPrivateKey(privateKeysetFile);

        ecies.generateAndStorePublicKey(privateKey, publicKeysetFile);
        publicKey = ecies.loadPublicKey(publicKeysetFile);
    }

    @Test
    void encryptionAndDecryptionWithValidInputsIsSuccessful() throws Exception {
        byte[] cipherText = ecies.encrypt(publicKey, INITIAL_TEXT, CONTEXT_INFO);
        byte[] plainText = ecies.decrypt(privateKey, cipherText, CONTEXT_INFO);

        Assertions.assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }
}