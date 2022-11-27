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
package de.dominikschadow.javasecurity.tink.aead;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

class AesEaxWithGeneratedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ASSOCIATED_DATA = "Some additional data".getBytes(StandardCharsets.UTF_8);

    private AesEaxWithGeneratedKey aes;

    @BeforeEach
    protected void setup() throws Exception {
        aes = new AesEaxWithGeneratedKey();
    }

    @Test
    void encryptionAndDecryptionWithValidInputsIsSuccessful() throws Exception {
        KeysetHandle secretKey = aes.generateKey();

        byte[] cipherText = aes.encrypt(secretKey, INITIAL_TEXT, ASSOCIATED_DATA);
        byte[] plainText = aes.decrypt(secretKey, cipherText, ASSOCIATED_DATA);

        Assertions.assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }

    @Test
    void decryptionWithInvalidAssociatedDataFails() throws Exception {
        KeysetHandle secretKey = aes.generateKey();

        byte[] cipherText = aes.encrypt(secretKey, INITIAL_TEXT, ASSOCIATED_DATA);

        Exception exception = assertThrows(GeneralSecurityException.class, () -> aes.decrypt(secretKey, cipherText, "manipulation".getBytes(StandardCharsets.UTF_8)));

        assertTrue(exception.getMessage().contains("decryption failed"));
    }
}