/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
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

import org.keyczar.Crypter;
import org.keyczar.exceptions.KeyczarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Asymmetric encryption sample with Keyczar. Loads the RSA key from the sample key set, encrypts and decrypts sample text with it.
 *
 * @author Dominik Schadow
 */
public class RSAEncryption {
    private static final Logger log = LoggerFactory.getLogger(RSAEncryption.class);
    private static final String KEYSET_PATH = "crypto-keyczar/src/main/resources/key-sets/encrypt/asymmetric";

    /**
     * Private constructor.
     */
    private RSAEncryption() {
    }

    public static void main(String[] args) {
        final String initialText = "Some dummy text for encryption";
        try {
            String ciphertext = encrypt(initialText);
            String plaintext = decrypt(ciphertext);

            printReadableMessages(initialText, ciphertext, plaintext);
        } catch (KeyczarException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * The encrypted String (ciphertext) returned is already encoded in Base64.
     *
     * @param initialText The text to encrypt (in UTF-8)
     * @return The encrypted text (in Base64)
     * @throws KeyczarException General Keyczar exception
     */
    private static String encrypt(String initialText) throws KeyczarException {
        Crypter crypter = new Crypter(KEYSET_PATH);
        return crypter.encrypt(initialText);
    }

    private static String decrypt(String ciphertext) throws KeyczarException {
        Crypter crypter = new Crypter(KEYSET_PATH);
        return crypter.decrypt(ciphertext);
    }

    private static void printReadableMessages(String initialText, String ciphertext, String plaintext) {
        log.info("initialText: {}", initialText);
        log.info("cipherText: {}", ciphertext);
        log.info("plaintext: {}", plaintext);
    }
}
