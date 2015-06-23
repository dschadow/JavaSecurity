/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Asymmetric encryption sample with plain Java. Loads the RSA key from the sample keystore, encrypts and decrypts sample text with it.
 * <p>
 * Uses Google Guava to Base64 print the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class RSA {
    private static final Logger LOGGER = LoggerFactory.getLogger(RSA.class);
    private static final String ALGORITHM = "RSA";
    private static final String KEYSTORE_PATH = "/samples.ks";

    public static void main(String[] args) {
        RSA aes = new RSA();
        final String initialText = "RSA encryption sample text";
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "asymmetric-sample-rsa";
        final char[] keyPassword = "asymmetric-sample-rsa".toCharArray();

        try {
            KeyStore ks = aes.loadKeystore(KEYSTORE_PATH, keystorePassword);
            PrivateKey privateKey = aes.loadPrivateKey(ks, keyAlias, keyPassword);
            PublicKey publicKey = aes.loadPublicKey(ks, keyAlias);

            byte[] ciphertext = aes.encrypt(publicKey, initialText);
            byte[] plaintext = aes.decrypt(privateKey, ciphertext);

            aes.printReadableMessages(initialText, ciphertext, plaintext);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException |
                KeyStoreException | CertificateException | UnrecoverableKeyException | InvalidKeyException | IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private KeyStore loadKeystore(String keystorePath, char[] keystorePassword) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        InputStream keystoreStream = getClass().getResourceAsStream(keystorePath);

        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(keystoreStream, keystorePassword);

        return ks;
    }

    private PrivateKey loadPrivateKey(KeyStore ks, String keyAlias, char[] keyPassword) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        if (!ks.containsAlias(keyAlias)) {
            throw new RuntimeException("Private key " + keyAlias + " not found in keystore");
        }

        PrivateKey key = (PrivateKey) ks.getKey(keyAlias, keyPassword);

        return key;
    }

    private PublicKey loadPublicKey(KeyStore ks, String keyAlias) throws KeyStoreException {
        if (!ks.containsAlias(keyAlias)) {
            throw new RuntimeException("Public key " + keyAlias + " not found in keystore");
        }

        PublicKey key = ks.getCertificate(keyAlias).getPublicKey();

        return key;
    }

    private byte[] encrypt(PublicKey publicKey, String initialText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedMessage = cipher.doFinal(initialText.getBytes("UTF-8"));

        return encryptedMessage;
    }

    private byte[] decrypt(PrivateKey privateKey, byte[] ciphertext) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedTextBytes = cipher.doFinal(ciphertext);

        return decryptedTextBytes;
    }

    private void printReadableMessages(String initialText, byte[] ciphertext, byte[] plaintext) {
        LOGGER.info("initialText: {}", initialText);
        LOGGER.info("cipherText as byte[]: {}", new String(ciphertext));
        LOGGER.info("cipherText as Base64: {}", BaseEncoding.base64().encode(ciphertext));
        LOGGER.info("plaintext: {}", new String(plaintext));
    }
}
