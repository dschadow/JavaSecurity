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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EciesWithGeneratedKeyAndKeyRotationTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final byte[] CONTEXT_INFO = "Some additional data".getBytes(StandardCharsets.UTF_8);

    private EciesWithGeneratedKeyAndKeyRotation ecies;

    @BeforeEach
    protected void setup() throws Exception {
        ecies = new EciesWithGeneratedKeyAndKeyRotation();
    }

    @Test
    void encryptionAndDecryptionWithValidInputsIsSuccessful() throws Exception {
        KeysetHandle originalKey = ecies.generatePrivateKey();
        KeysetHandle rotatedKey = ecies.rotateKey(originalKey);
        KeysetHandle publicKey = ecies.generatePublicKey(rotatedKey);

        byte[] cipherText = ecies.encrypt(publicKey, INITIAL_TEXT, CONTEXT_INFO);
        byte[] plainText = ecies.decrypt(rotatedKey, cipherText, CONTEXT_INFO);

        Assertions.assertAll(
                () -> assertNotEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(cipherText, StandardCharsets.UTF_8)),
                () -> assertNotEquals(originalKey.getKeysetInfo().getPrimaryKeyId(), rotatedKey.getKeysetInfo().getPrimaryKeyId()),
                () -> assertEquals(new String(INITIAL_TEXT, StandardCharsets.UTF_8), new String(plainText, StandardCharsets.UTF_8))
        );
    }
}