package de.dominikschadow.javasecurity;

import de.dominikschadow.javasecurity.symmetric.AES;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class Keystore {
    private static final String KEYSTORE_PATH = "/samples.ks";

    public static KeyStore loadKeystore(char[] keystorePassword) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        try (InputStream keystoreStream = AES.class.getResourceAsStream(KEYSTORE_PATH)) {
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(keystoreStream, keystorePassword);

            return ks;
        }
    }

    public static Key loadKey(KeyStore ks, String keyAlias, char[] keyPassword) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        if (!ks.containsAlias(keyAlias)) {
            throw new UnrecoverableKeyException("Secret key " + keyAlias + " not found in keystore");
        }

        return ks.getKey(keyAlias, keyPassword);
    }
}
