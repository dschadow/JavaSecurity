/*
 * Copyright (C) 2022 Dominik Schadow, dominikschadow@gmail.com
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

import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

import java.security.Key;

/**
 * Symmetric encryption sample with Apache Shiro. Loads the AES key from the sample keystore, encrypts and decrypts sample text with it.
 *
 * @author Dominik Schadow
 */
public class AES {
    /**
     * Encrypts the given text using all Shiro defaults: 128 bit size, CBC mode, PKCS5 padding scheme.
     *
     * @param key         The key to use
     * @param initialText The text to encrypt
     * @return The encrypted text
     */
    public byte[] encrypt(Key key, byte[] initialText) {
        AesCipherService cipherService = new AesCipherService();
        ByteSource cipherText = cipherService.encrypt(initialText, key.getEncoded());

        return cipherText.getBytes();
    }

    public byte[] decrypt(Key key, byte[] ciphertext) {
        AesCipherService cipherService = new AesCipherService();
        ByteSource plainText = cipherService.decrypt(ciphertext, key.getEncoded());

        return plainText.getBytes();
    }
}
