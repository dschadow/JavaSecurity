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
package de.dominikschadow.javasecurity.asymmetric;

import de.dominikschadow.javasecurity.Keystore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DSATest {
    private final DSA dsa = new DSA();
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @BeforeEach
    protected void setup() throws Exception {
        final char[] keystorePassword = "samples".toCharArray();
        final String keyAlias = "asymmetric-sample-dsa";
        final char[] keyPassword = "asymmetric-sample-dsa".toCharArray();

        KeyStore ks = Keystore.loadKeystore(keystorePassword);
        privateKey = Keystore.loadPrivateKey(ks, keyAlias, keyPassword);
        publicKey = Keystore.loadPublicKey(ks, keyAlias);
    }

    @Test
    void givenIdenticalTextWhenVerifyingSignatureThenReturnTrue() throws Exception {
        final String initialText = "DSA signature sample text";

        byte[] signature = dsa.sign(privateKey, initialText);
        boolean validSignature = dsa.verify(publicKey, signature, initialText);

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertTrue(validSignature)
        );
    }

    @Test
    void givenNotIdenticalTextWhenComparingHashesThenReturnFalse() throws Exception {
        final String initialText = "DSA signature sample text";

        byte[] signature = dsa.sign(privateKey, initialText);
        boolean validSignature = dsa.verify(publicKey, signature, "FakeText");

        Assertions.assertAll(
                () -> assertTrue(signature.length > 0),
                () -> assertFalse(validSignature)
        );
    }
}