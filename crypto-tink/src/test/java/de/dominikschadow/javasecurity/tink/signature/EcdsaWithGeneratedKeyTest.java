package de.dominikschadow.javasecurity.tink.signature;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EcdsaWithGeneratedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);

    private  EcdsaWithGeneratedKey ecdsa ;

    @BeforeEach
    protected void setup() throws Exception {
        ecdsa = new EcdsaWithGeneratedKey();
    }

    @Test
    void unchangedInputValidatesSuccessful() throws Exception {
        KeysetHandle privateKey = ecdsa.generatePrivateKey();
        KeysetHandle publicKey = ecdsa.generatePublicKey(privateKey);

        byte[] signature = ecdsa.sign(privateKey, INITIAL_TEXT);
        boolean validation = ecdsa.verify(publicKey, signature, INITIAL_TEXT);

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertTrue(validation)
        );
    }

    @Test
    void changedInputValidationFails() throws Exception {
        KeysetHandle privateKey = ecdsa.generatePrivateKey();
        KeysetHandle publicKey = ecdsa.generatePublicKey(privateKey);

        byte[] signature = ecdsa.sign(privateKey, INITIAL_TEXT);
        boolean validation = ecdsa.verify(publicKey, signature, "Manipulation".getBytes(StandardCharsets.UTF_8));

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertFalse(validation)
        );
    }
}