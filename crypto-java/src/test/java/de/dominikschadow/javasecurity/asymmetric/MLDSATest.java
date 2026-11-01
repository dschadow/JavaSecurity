/*
 * Copyright (C) 2026 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.asymmetric;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MLDSATest {
    private final MLDSA mldsa = new MLDSA();
    private KeyPair keyPair;

    @BeforeEach
    protected void setup() throws Exception {
        keyPair = mldsa.generateKeyPair();
    }

    @Test
    void givenIdenticalTextWhenVerifyingSignatureThenReturnTrue() throws Exception {
        final String initialText = "ML-DSA signature sample text";

        byte[] signature = mldsa.sign(keyPair.getPrivate(), initialText);
        boolean validSignature = mldsa.verify(keyPair.getPublic(), signature, initialText);

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertTrue(validSignature)
        );
    }

    @Test
    void givenNotIdenticalTextWhenVerifyingSignatureThenReturnFalse() throws Exception {
        final String initialText = "ML-DSA signature sample text";

        byte[] signature = mldsa.sign(keyPair.getPrivate(), initialText);
        boolean validSignature = mldsa.verify(keyPair.getPublic(), signature, "FakeText");

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertFalse(validSignature)
        );
    }

    @Test
    void givenOtherPublicKeyWhenVerifyingSignatureThenReturnFalse() throws Exception {
        final String initialText = "ML-DSA signature sample text";

        byte[] signature = mldsa.sign(keyPair.getPrivate(), initialText);
        boolean validSignature = mldsa.verify(mldsa.generateKeyPair().getPublic(), signature, initialText);

        assertFalse(validSignature);
    }
}
