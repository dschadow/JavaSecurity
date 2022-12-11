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
package de.dominikschadow.javasecurity.tink.hybrid;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

class EciesWithGeneratedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] CONTEXT_INFO = "Some additional data".getBytes(StandardCharsets.UTF_8);

    private EciesWithGeneratedKey ecies;

    @BeforeEach
    protected void setup() throws Exception {
        ecies = new EciesWithGeneratedKey();
    }

    @Test
    void encryptionAndDecryptionWithValidInputsIsSuccessful() throws Exception {
        KeysetHandle privateKey = ecies.generatePrivateKey();
        KeysetHandle publicKey = ecies.generatePublicKey(privateKey);

        byte[] cipherText = ecies.encrypt(publicKey, INITIAL_TEXT, CONTEXT_INFO);
        byte[] plainText = ecies.decrypt(privateKey, cipherText, CONTEXT_INFO);

        Assertions.assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }

    @Test
    void decryptionWithInvalidAssociatedDataFails() throws Exception {
        KeysetHandle privateKey = ecies.generatePrivateKey();
        KeysetHandle publicKey = ecies.generatePublicKey(privateKey);

        byte[] cipherText = ecies.encrypt(publicKey, INITIAL_TEXT, CONTEXT_INFO);

        Exception exception = assertThrows(GeneralSecurityException.class, () -> ecies.decrypt(privateKey, cipherText, "manipulation".getBytes(StandardCharsets.UTF_8)));

        assertTrue(exception.getMessage().contains("decryption failed"));
    }
}