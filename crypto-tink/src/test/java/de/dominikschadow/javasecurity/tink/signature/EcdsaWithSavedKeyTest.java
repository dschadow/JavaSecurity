package de.dominikschadow.javasecurity.tink.signature;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EcdsaWithSavedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final String PRIVATE_KEYSET_FILENAME = "src/test/resources/keysets/signature-ecdsa-private.json";
    private static final String PUBLIC_KEYSET_FILENAME = "src/test/resources/keysets/signature-ecdsa-public.json";
    private final File privateKeysetFile = new File(PRIVATE_KEYSET_FILENAME);
    private final File publicKeysetFile = new File(PUBLIC_KEYSET_FILENAME);
    private KeysetHandle publicKey;
    private KeysetHandle privateKey;

    private EcdsaWithSavedKey ecdsa;

    @BeforeEach
    protected void setup() throws Exception {
        ecdsa = new EcdsaWithSavedKey();

        ecdsa.generateAndStorePrivateKey(privateKeysetFile);
        privateKey = ecdsa.loadPrivateKey(privateKeysetFile);

        ecdsa.generateAndStorePublicKey(privateKey, publicKeysetFile);
        publicKey = ecdsa.loadPublicKey(publicKeysetFile);
    }

    @Test
    void unchangedInputValidatesSuccessful() throws Exception {
        byte[] signature = ecdsa.sign(privateKey, INITIAL_TEXT);
        boolean validation = ecdsa.verify(publicKey, signature, INITIAL_TEXT);

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertTrue(validation)
        );
    }

    @Test
    void changedInputValidationFails() throws Exception {
        byte[] signature = ecdsa.sign(privateKey, INITIAL_TEXT);
        boolean validation = ecdsa.verify(publicKey, signature, "Manipulation".getBytes(StandardCharsets.UTF_8));

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertFalse(validation)
        );
    }
}