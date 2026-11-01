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
package de.dominikschadow.javasecurity.kdf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;

class HKDFTest {
    private final HKDF hkdf = new HKDF();

    /**
     * Test case 1 from RFC 5869, appendix A.1.
     */
    @Test
    void givenRfc5869TestVectorWhenDerivingDataThenReturnExpectedOutput() throws Exception {
        HexFormat hex = HexFormat.of();
        byte[] ikm = hex.parseHex("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b");
        byte[] salt = hex.parseHex("000102030405060708090a0b0c");
        byte[] info = hex.parseHex("f0f1f2f3f4f5f6f7f8f9");
        String expectedOkm = "3cb25f25faacd57a90434f64d0362f2a2d2d0a90cf1a5a4c5db02d56ecc4c5bf34007208d5b887185865";

        byte[] okm = hkdf.deriveData(ikm, salt, info, 42);

        assertEquals(expectedOkm, hex.formatHex(okm));
    }

    @Test
    void givenIdenticalInputWhenDerivingKeyThenReturnIdenticalKey() throws Exception {
        byte[] ikm = "shared secret from ML-KEM or ECDH".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);
        byte[] info = "encryption".getBytes(StandardCharsets.UTF_8);

        SecretKey first = hkdf.deriveKey(ikm, salt, info);
        SecretKey second = hkdf.deriveKey(ikm, salt, info);

        Assertions.assertAll(
                () -> assertEquals("AES", first.getAlgorithm()),
                () -> assertEquals(32, first.getEncoded().length),
                () -> assertArrayEquals(first.getEncoded(), second.getEncoded())
        );
    }

    @Test
    void givenDifferentInfoWhenDerivingKeyThenReturnDifferentKeys() throws Exception {
        byte[] ikm = "shared secret from ML-KEM or ECDH".getBytes(StandardCharsets.UTF_8);
        byte[] salt = "salt".getBytes(StandardCharsets.UTF_8);

        SecretKey encryptionKey = hkdf.deriveKey(ikm, salt, "encryption".getBytes(StandardCharsets.UTF_8));
        SecretKey macKey = hkdf.deriveKey(ikm, salt, "authentication".getBytes(StandardCharsets.UTF_8));

        assertFalse(Arrays.equals(encryptionKey.getEncoded(), macKey.getEncoded()));
    }
}
