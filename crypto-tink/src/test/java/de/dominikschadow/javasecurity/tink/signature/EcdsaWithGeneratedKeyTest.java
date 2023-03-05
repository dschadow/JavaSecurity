/*
 * Copyright (C) 2023 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dominikschadow.javasecurity.tink.signature;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EcdsaWithGeneratedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);

    private  EcdsaWithGeneratedKey ecdsa ;

    @BeforeEach
    protected void setup() throws Exception {
        ecdsa = new EcdsaWithGeneratedKey();
    }

    @Test
    void unchangedInputValidatesSuccessful() throws Exception {
        KeysetHandle privateKey = ecdsa.generatePrivateKey();
        KeysetHandle publicKey = ecdsa.generatePublicKey(privateKey);

        byte[] signature = ecdsa.sign(privateKey, INITIAL_TEXT);
        boolean validation = ecdsa.verify(publicKey, signature, INITIAL_TEXT);

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertTrue(validation)
        );
    }

    @Test
    void changedInputValidationFails() throws Exception {
        KeysetHandle privateKey = ecdsa.generatePrivateKey();
        KeysetHandle publicKey = ecdsa.generatePublicKey(privateKey);

        byte[] signature = ecdsa.sign(privateKey, INITIAL_TEXT);
        boolean validation = ecdsa.verify(publicKey, signature, "Manipulation".getBytes(StandardCharsets.UTF_8));

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertFalse(validation)
        );
    }
}