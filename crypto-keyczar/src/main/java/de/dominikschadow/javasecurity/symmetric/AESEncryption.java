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
package de.dominikschadow.javasecurity.symmetric;

import org.keyczar.Crypter;
import org.keyczar.exceptions.KeyczarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Symmetric encryption sample with Keyczar. Loads the AES key from the sample key set, encrypts and decrypts sample
 * text with it.
 *
 * @author Dominik Schadow
 */
public class AESEncryption {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESEncryption.class);
    private static final String KEYSET_PATH = "crypto-keyczar/src/main/resources/key-sets/encrypt/asymmetric";

    public static void main(String[] args) {
        AESEncryption res = new AESEncryption();
        final String initialText = "Some dummy text for encryption";
        try {
            String ciphertext = res.encrypt(initialText);
            String plaintext = res.decrypt(ciphertext);

            res.printReadableMessages(initialText, ciphertext, plaintext);
        } catch (KeyczarException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    /**
     * The encrypted String (ciphertext) returned is already encoded in Base64.
     *
     * @param initialText The text to encrypt (in UTF-8)
     * @return The encrypted text (in Base64)
     * @throws KeyczarException
     */
    private String encrypt(String initialText) throws KeyczarException {
        Crypter crypter = new Crypter(KEYSET_PATH);
        return crypter.encrypt(initialText);
    }

    private String decrypt(String ciphertext) throws KeyczarException {
        Crypter crypter = new Crypter(KEYSET_PATH);
        return crypter.decrypt(ciphertext);
    }

    private void printReadableMessages(String initialText, String ciphertext, String plaintext) {
        LOGGER.info("initialText: {}", initialText);
        LOGGER.info("cipherText as Base64: {}", ciphertext);
        LOGGER.info("plaintext: {}", plaintext);
    }
}
