/*
 * Copyright (C) 2018 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.symmetric;

import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Symmetric encryption sample with Apache Shiro. Loads the AES key from the sample keystore, encrypts and decrypts sample text with it.
 *
 * @author Dominik Schadow
 */
public class AES {
    private static final Logger log = LoggerFactory.getLogger(AES.class);
    private static final String KEYSTORE_PATH = "/samples.ks";

    /**
     * Private constructor.
     */
    private AES() {
    }

    public static void main(String[] args) {
        final String initialText = "AES encryption sample text";
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "symmetric-sample";
        final char[] keyPassword = "symmetric-sample".toCharArray();

        try {
            KeyStore ks = loadKeystore(keystorePassword);
            Key key = loadKey(ks, keyAlias, keyPassword);
            byte[] ciphertext = encrypt(key, CodecSupport.toBytes(initialText));
            byte[] plaintext = decrypt(key, ciphertext);

            printReadableMessages(initialText, ciphertext, plaintext);
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | UnrecoverableKeyException | IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private static KeyStore loadKeystore(char[] keystorePassword) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        InputStream keystoreStream = AES.class.getResourceAsStream(KEYSTORE_PATH);

        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(keystoreStream, keystorePassword);

        return ks;
    }

    private static Key loadKey(KeyStore ks, String keyAlias, char[] keyPassword) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        if (!ks.containsAlias(keyAlias)) {
            throw new UnrecoverableKeyException("Secret key " + keyAlias + " not found in keystore");
        }

        return ks.getKey(keyAlias, keyPassword);
    }

    /**
     * Encrypts the given text using all Shiro defaults: 128 bit size, CBC mode, PKCS5 padding scheme.
     *
     * @param key         The key to use
     * @param initialText The text to encrypt
     * @return The encrypted text
     */
    private static byte[] encrypt(Key key, byte[] initialText) {
        AesCipherService cipherService = new AesCipherService();
        ByteSource cipherText = cipherService.encrypt(initialText, key.getEncoded());

        return cipherText.getBytes();
    }

    private static byte[] decrypt(Key key, byte[] ciphertext) {
        AesCipherService cipherService = new AesCipherService();
        ByteSource plainText = cipherService.decrypt(ciphertext, key.getEncoded());

        return plainText.getBytes();
    }

    private static void printReadableMessages(String initialText, byte[] ciphertext, byte[] plaintext) {
        log.info("initialText: {}", initialText);
        log.info("cipherText as HEX: {}", Hex.encodeToString(ciphertext));
        log.info("plaintext: {}", CodecSupport.toString(plaintext));
    }
}
