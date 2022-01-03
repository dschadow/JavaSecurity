package de.dominikschadow.javasecurity.asymmetric;

import de.dominikschadow.javasecurity.Keystore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RSATest {
    private final RSA rsa = new RSA();
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @BeforeEach
    protected void setup() throws Exception {
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "asymmetric-sample-rsa";
        final char[] keyPassword = "asymmetric-sample-rsa".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        privateKey = Keystore.loadPrivateKey(ks, keyAlias, keyPassword);
        publicKey = Keystore.loadPublicKey(ks, keyAlias);
    }

    @Test
    void givenCorrectCiphertextWhenDecryptingThenReturnPlaintext() throws Exception {
        final String initialText = "RSA encryption sample text";

        byte[] ciphertext = rsa.encrypt(publicKey, initialText);
        byte[] plaintext = rsa.decrypt(privateKey, ciphertext);

        Assertions.assertAll(
                () -> assertNotEquals(initialText, new String(ciphertext)),
                () -> assertEquals(initialText, new String(plaintext))
        );
    }
}