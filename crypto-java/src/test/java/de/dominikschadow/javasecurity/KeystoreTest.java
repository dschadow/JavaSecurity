package de.dominikschadow.javasecurity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;

import static org.junit.jupiter.api.Assertions.*;

class KeystoreTest {
    private final char[] keystorePassword = "samples".toCharArray();

    @Test
    void givenValidPasswordWhenLoadingKeyStoreThenReturnKeystore() throws Exception {
        KeyStore ks = Keystore.loadKeystore(keystorePassword);

        assertNotNull(ks);
    }

    @Test
    void givenInvalidPasswordWhenLoadingKeyStoreThenThrowException() {
        Exception exception = assertThrows(IOException.class, () -> Keystore.loadKeystore("wrongPassword".toCharArray()));

        assertEquals("Keystore was tampered with, or password was incorrect", exception.getMessage());
    }

    @Test
    void givenValidAliasAndPasswordWhenLoadingPrivateKeyThenReturnKey() throws Exception {
        final String keyAlias = "asymmetric-sample-rsa";
        final char[] keyPassword = "asymmetric-sample-rsa".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        PrivateKey privateKey = Keystore.loadPrivateKey(ks, keyAlias, keyPassword);

        Assertions.assertAll(
                () -> assertNotNull(privateKey),
                () -> assertEquals("RSA", privateKey.getAlgorithm())
        );
    }

    @Test
    void givenUnknownAliasWhenLoadingPrivateKeyThenThrowException() throws Exception {
        final String keyAlias = "unknown";
        final char[] keyPassword = "asymmetric-sample-rsa".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        Exception exception = assertThrows(UnrecoverableKeyException.class, () -> Keystore.loadPrivateKey(ks, keyAlias, keyPassword));

        assertEquals("Private key unknown not found in keystore", exception.getMessage());
    }

    @Test
    void givenValidAliasWhenLoadingPublicKeyThenReturnKey() throws Exception {
        final String keyAlias = "asymmetric-sample-rsa";

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        PublicKey publicKey = Keystore.loadPublicKey(ks, keyAlias);

        Assertions.assertAll(
                () -> assertNotNull(publicKey),
                () -> assertEquals("RSA", publicKey.getAlgorithm())
        );
    }

    @Test
    void givenUnknownAliasWhenLoadingPublicKeyThenThrowException() throws Exception {
        final String keyAlias = "unknown";

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        Exception exception = assertThrows(UnrecoverableKeyException.class, () -> Keystore.loadPublicKey(ks, keyAlias));

        assertEquals("Public key unknown not found in keystore", exception.getMessage());
    }

    @Test
    void givenValidAliasAndPasswordWhenLoadingKeyThenReturnKey() throws Exception {
        final String keyAlias = "symmetric-sample";
        final char[] keyPassword = "symmetric-sample".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        Key key = Keystore.loadKey(ks, keyAlias, keyPassword);

        Assertions.assertAll(
                () -> assertNotNull(key),
                () -> assertEquals("AES", key.getAlgorithm())
        );
    }

    @Test
    void givenUnknownAliasWhenLoadingKeyThenThrowException() throws Exception {
        final String keyAlias = "unknown";
        final char[] keyPassword = "symmetric-sample".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        Exception exception = assertThrows(UnrecoverableKeyException.class, () -> Keystore.loadKey(ks, keyAlias, keyPassword));

        assertEquals("Secret key unknown not found in keystore", exception.getMessage());
    }

    @Test
    void givenValidAliasAndInvalidPasswordWhenLoadingKeyThenThrowException() throws Exception {
        final String keyAlias = "symmetric-sample";
        final char[] keyPassword = "wrongPassword".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        Exception exception = assertThrows(UnrecoverableKeyException.class, () -> Keystore.loadKey(ks, keyAlias, keyPassword));

        assertEquals("Given final block not properly padded. Such issues can arise if a bad key is used during decryption.", exception.getMessage());
    }

    @Test
    void givenValidKeyAndAlgorithmWhenCreatingSecretKeySpecThenReturnSecretKeySpec() throws Exception {
        final String keyAlias = "symmetric-sample";
        final char[] keyPassword = "symmetric-sample".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        Key key = Keystore.loadKey(ks, keyAlias, keyPassword);

        SecretKeySpec secretKeySpec = Keystore.createSecretKeySpec(key.getEncoded(), "AES");

        Assertions.assertAll(
                () -> assertNotNull(secretKeySpec),
                () -> assertEquals("AES", secretKeySpec.getAlgorithm())
        );
    }
}