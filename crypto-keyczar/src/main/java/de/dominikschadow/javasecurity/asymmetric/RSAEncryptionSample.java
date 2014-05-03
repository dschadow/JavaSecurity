/*
 * Copyright (C) 2014 Dominik Schadow, dominikschadow@gmail.com
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
public class RSAEncryptionSample {
    private static final Logger logger = LoggerFactory.getLogger(RSAEncryptionSample.class);
    private static final String KEYSET_PATH = "crypto-keyczar/src/main/resources/key-sets/encrypt/symmetric";

    public static void main(String[] args) {
        RSAEncryptionSample res = new RSAEncryptionSample();
        final String initialText = "Encrypt me at Java Forum Stuttgart 2014";
        try {
            String ciphertext = res.encrypt(initialText);
            String plaintext = res.decrypt(ciphertext);

            res.printReadableMessages(initialText, ciphertext, plaintext);
        } catch (KeyczarException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private String encrypt(String initialText) throws KeyczarException {
        Crypter crypter = new Crypter(KEYSET_PATH);
        String ciphertext = crypter.encrypt(initialText);

        return ciphertext;
    }

    private String decrypt(String ciphertext) throws KeyczarException {
        Crypter crypter = new Crypter(KEYSET_PATH);
        String plaintext = crypter.decrypt(ciphertext);

        return plaintext;
    }

    private void printReadableMessages(String initialText, String ciphertext, String plaintext) {
        logger.info("initialText {}", initialText);
        logger.info("cipherText {}", ciphertext);
        logger.info("plaintext {}", plaintext);
    }
}
