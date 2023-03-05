/*
 * Copyright (C) 2023 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.tink.signature;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EcdsaWithSavedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);
    private static final String PRIVATE_KEYSET_FILENAME = "src/test/resources/keysets/signature-ecdsa-private.json";
    private static final String PUBLIC_KEYSET_FILENAME = "src/test/resources/keysets/signature-ecdsa-public.json";
    private final File privateKeysetFile = new File(PRIVATE_KEYSET_FILENAME);
    private final File publicKeysetFile = new File(PUBLIC_KEYSET_FILENAME);
    private KeysetHandle publicKey;
    private KeysetHandle privateKey;

    private EcdsaWithSavedKey ecdsa;

    @BeforeEach
    protected void setup() throws Exception {
        ecdsa = new EcdsaWithSavedKey();

        ecdsa.generateAndStorePrivateKey(privateKeysetFile);
        privateKey = ecdsa.loadPrivateKey(privateKeysetFile);

        ecdsa.generateAndStorePublicKey(privateKey, publicKeysetFile);
        publicKey = ecdsa.loadPublicKey(publicKeysetFile);
    }

    @Test
    void unchangedInputValidatesSuccessful() throws Exception {
        byte[] signature = ecdsa.sign(privateKey, INITIAL_TEXT);
        boolean validation = ecdsa.verify(publicKey, signature, INITIAL_TEXT);

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertTrue(validation)
        );
    }

    @Test
    void changedInputValidationFails() throws Exception {
        byte[] signature = ecdsa.sign(privateKey, INITIAL_TEXT);
        boolean validation = ecdsa.verify(publicKey, signature, "Manipulation".getBytes(StandardCharsets.UTF_8));

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertFalse(validation)
        );
    }
}