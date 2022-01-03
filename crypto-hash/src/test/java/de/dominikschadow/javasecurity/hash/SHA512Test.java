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
package de.dominikschadow.javasecurity.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SHA512Test {
    private final SHA512 sha512 = new SHA512();

    @Test
    void givenIdenticalPasswordsWhenComparingHashesReturnsTrue() throws Exception {
        String password = "TotallySecurePassword12345";

        byte[] salt = sha512.generateSalt();
        byte[] originalHash = sha512.calculateHash(password, salt);
        boolean hashMatches = sha512.verifyPassword(originalHash, password, salt);

        Assertions.assertAll(
                () -> assertNotNull(salt),
                () -> assertNotNull(originalHash),
                () -> assertTrue(hashMatches)
        );
    }

    @Test
    void givenNotIdenticalPasswordsWhenComparingHashesReturnsFalse() throws Exception {
        String password = "TotallySecurePassword12345";

        byte[] salt = sha512.generateSalt();
        byte[] originalHash = sha512.calculateHash(password, salt);
        boolean hashMatches = sha512.verifyPassword(originalHash, "fakePassword12345", salt);

        Assertions.assertAll(
                () -> assertNotNull(salt),
                () -> assertNotNull(originalHash),
                () -> assertFalse(hashMatches)
        );
    }
}