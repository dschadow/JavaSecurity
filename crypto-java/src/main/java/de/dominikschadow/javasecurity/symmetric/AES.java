/*
 * Copyright (C) 2020 Dominik Schadow, dominikschadow@gmail.com
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

import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Symmetric encryption sample with plain Java. Loads the AES key from the sample keystore, encrypts and decrypts sample
 * text with it.
 * <p/>
 * Note that the {@code INITIALIZATION_VECTOR} is not stored. One possibility to store it is to prepend it to the
 * encrypted message with a delimiter (all in Base64 encoding): {@code Base64(IV) + DELIMITER + Base64(ENCRYPTED *
 * MESSAGE)}
 * <p/>
 * Uses Google Guava to hex the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class AES {
    private static final Logger log = LoggerFactory.getLogger(AES.class);
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEYSTORE_PATH = "/samples.ks";
    private Cipher cipher;

    public static void main(String[] args) {
        AES aes = new AES();
        aes.encrypt();
    }

    private void encrypt() {
        final String initialText = "AES encryption sample text";
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "symmetric-sample";
        final char[] keyPassword = "symmetric-sample".toCharArray();

        try {
            cipher = Cipher.getInstance(ALGORITHM);
            KeyStore ks = loadKeystore(keystorePassword);
            Key key = loadKey(ks, keyAlias, keyPassword);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "AES");
            byte[] ciphertext = encrypt(secretKeySpec, initialText);
            byte[] plaintext = decrypt(secretKeySpec, ciphertext);

            printReadableMessages(initialText, ciphertext, plaintext);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException |
                KeyStoreException | CertificateException | UnrecoverableKeyException |
                InvalidAlgorithmParameterException | InvalidKeyException | IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private KeyStore loadKeystore(char[] keystorePassword) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException {
        try (InputStream keystoreStream = getClass().getResourceAsStream(KEYSTORE_PATH)) {
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(keystoreStream, keystorePassword);

            return ks;
        }
    }

    private static Key loadKey(KeyStore ks, String keyAlias, char[] keyPassword) throws KeyStoreException,
            UnrecoverableKeyException, NoSuchAlgorithmException {
        if (!ks.containsAlias(keyAlias)) {
            throw new UnrecoverableKeyException("Secret key " + keyAlias + " not found in keystore");
        }

        return ks.getKey(keyAlias, keyPassword);
    }

    private byte[] encrypt(SecretKeySpec secretKeySpec, String initialText) throws
            BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(initialText.getBytes(StandardCharsets.UTF_8));
    }

    private byte[] decrypt(SecretKeySpec secretKeySpec, byte[] ciphertext) throws
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(cipher.getIV()));
        return cipher.doFinal(ciphertext);
    }

    private static void printReadableMessages(String initialText, byte[] ciphertext, byte[] plaintext) {
        log.info("initial text: {}", initialText);
        log.info("cipher text: {}", BaseEncoding.base16().encode(ciphertext));
        log.info("plain text: {}", new String(plaintext, StandardCharsets.UTF_8));
    }
}
