package de.dominikschadow.javasecurity.symmetric;

import de.dominikschadow.javasecurity.Keystore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AESTest {
    private AES aes;

    @BeforeEach
    protected void setup() throws Exception {
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "symmetric-sample";
        final char[] keyPassword = "symmetric-sample".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        Key key = Keystore.loadKey(ks, keyAlias, keyPassword);
        SecretKeySpec secretKeySpec = Keystore.createSecretKeySpec(key.getEncoded(), "AES");

        aes = new AES(secretKeySpec, "AES/CBC/PKCS5Padding");
    }

    @Test
    void givenCorrectCiphertextWhenDecryptingThenReturnPlaintext() throws Exception {
        final String initialText = "AES encryption sample text";

        byte[] ciphertext = aes.encrypt(initialText);
        byte[] plaintext = aes.decrypt(ciphertext);

        Assertions.assertAll(
                () -> assertNotEquals(initialText, new String(ciphertext)),
                () -> assertEquals(initialText, new String(plaintext))
        );
    }
}