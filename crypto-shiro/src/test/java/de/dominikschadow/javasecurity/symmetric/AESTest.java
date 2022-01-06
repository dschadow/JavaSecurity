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

import de.dominikschadow.javasecurity.Keystore;
import org.apache.shiro.codec.CodecSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.security.KeyStore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AESTest {
    private final AES aes = new AES();
    private Key key;

    @BeforeEach
    protected void setup() throws Exception {
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "symmetric-sample";
        final char[] keyPassword = "symmetric-sample".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        key = Keystore.loadKey(ks, keyAlias, keyPassword);
    }

    @Test
    void givenCorrectCiphertextWhenDecryptingThenReturnPlaintext() {
        final String initialText = "AES encryption sample text";

            byte[] ciphertext = aes.encrypt(key, CodecSupport.toBytes(initialText));
            byte[] plaintext = aes.decrypt(key, ciphertext);

        Assertions.assertAll(
                () -> assertNotEquals(initialText, new String(ciphertext)),
                () -> assertEquals(initialText, new String(plaintext))
        );
    }
}