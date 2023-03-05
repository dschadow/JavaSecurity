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
package de.dominikschadow.javasecurity.tink.mac;

import com.google.crypto.tink.KeysetHandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HmacShaWithGeneratedKeyTest {
    private static final byte[] INITIAL_TEXT = "Some dummy text to work with".getBytes(StandardCharsets.UTF_8);

    private HmacShaWithGeneratedKey hmac;

    @BeforeEach
    protected void setup() throws Exception {
        hmac = new HmacShaWithGeneratedKey();
    }

    @Test
    void unchangedInputValidatesSuccessful() throws Exception {
        KeysetHandle keysetHandle = hmac.generateKey();

        byte[] initialMac = hmac.computeMac(keysetHandle, INITIAL_TEXT);
        boolean validation = hmac.verifyMac(keysetHandle, initialMac, INITIAL_TEXT);

        Assertions.assertAll(
                () -> assertNotNull(initialMac),
                () -> assertTrue(validation)
        );
    }

    @Test
    void changedInputValidationFails() throws Exception {
        KeysetHandle keysetHandle = hmac.generateKey();

        byte[] initialMac = hmac.computeMac(keysetHandle, INITIAL_TEXT);
        boolean validation = hmac.verifyMac(keysetHandle, initialMac, "manipulation".getBytes(StandardCharsets.UTF_8));

        Assertions.assertAll(
                () -> assertNotNull(initialMac),
                () -> assertFalse(validation)
        );
    }
}