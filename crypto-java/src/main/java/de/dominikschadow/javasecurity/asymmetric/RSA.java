/*
 * Copyright (C) 2021 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dominikschadow.javasecurity.asymmetric;

import com.google.common.io.BaseEncoding;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Asymmetric encryption sample with plain Java. Loads the RSA key from the sample keystore, encrypts and decrypts
 * sample text with it.
 * <p/>
 * Uses Google Guava to hex the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class RSA {
    private static final System.Logger LOG = System.getLogger(RSA.class.getName());
    private static final String ALGORITHM = "RSA";
    private static final String KEYSTORE_PATH = "/samples.ks";

    /**
     * Private constructor.
     */
    private RSA() {
    }

    public static void main(String[] args) {
        encrypt();
    }

    private static void encrypt() {
        final String initialText = "RSA encryption sample text";
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "asymmetric-sample-rsa";
        final char[] keyPassword = "asymmetric-sample-rsa".toCharArray();

        try {
            KeyStore ks = loadKeystore(keystorePassword);
            PrivateKey privateKey = loadPrivateKey(ks, keyAlias, keyPassword);
            PublicKey publicKey = loadPublicKey(ks, keyAlias);

            byte[] ciphertext = encrypt(publicKey, initialText);
            byte[] plaintext = decrypt(privateKey, ciphertext);

            printReadableMessages(initialText, ciphertext, plaintext);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException |
                KeyStoreException | CertificateException | UnrecoverableKeyException | InvalidKeyException |
                IOException ex) {
            LOG.log(System.Logger.Level.ERROR, ex.getMessage(), ex);
        }
    }

    private static KeyStore loadKeystore(char[] keystorePassword) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException {
        try (InputStream keystoreStream = RSA.class.getResourceAsStream(KEYSTORE_PATH)) {
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(keystoreStream, keystorePassword);
            return ks;
        }
    }

    private static PrivateKey loadPrivateKey(KeyStore ks, String keyAlias, char[] keyPassword) throws KeyStoreException,
            UnrecoverableKeyException, NoSuchAlgorithmException {
        if (!ks.containsAlias(keyAlias)) {
            throw new UnrecoverableKeyException("Private key " + keyAlias + " not found in keystore");
        }

        return (PrivateKey) ks.getKey(keyAlias, keyPassword);
    }

    private static PublicKey loadPublicKey(KeyStore ks, String keyAlias) throws KeyStoreException, UnrecoverableKeyException {
        if (!ks.containsAlias(keyAlias)) {
            throw new UnrecoverableKeyException("Public key " + keyAlias + " not found in keystore");
        }

        return ks.getCertificate(keyAlias).getPublicKey();
    }

    private static byte[] encrypt(PublicKey publicKey, String initialText) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(initialText.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] decrypt(PrivateKey privateKey, byte[] ciphertext) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(ciphertext);
    }

    private static void printReadableMessages(String initialText, byte[] ciphertext, byte[] plaintext) {
        LOG.log(System.Logger.Level.INFO, "initial text: {0}", initialText);
        LOG.log(System.Logger.Level.INFO, "cipher text: {0}", BaseEncoding.base16().encode(ciphertext));
        LOG.log(System.Logger.Level.INFO, "plain text: {0}", new String(plaintext, StandardCharsets.UTF_8));
    }
}
