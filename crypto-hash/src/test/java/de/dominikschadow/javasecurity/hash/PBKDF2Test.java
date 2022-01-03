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

import javax.crypto.SecretKeyFactory;

import static org.junit.jupiter.api.Assertions.*;

class PBKDF2Test {
    private final PBKDF2 pbkdf2 = new PBKDF2();

    @Test
    void givenIdenticalPasswordsWhenComparingHashesReturnsTrue() throws Exception {
        char[] password = "TotallySecurePassword12345".toCharArray();

        SecretKeyFactory skf = pbkdf2.createSecretKeyFactory();
        byte[] salt = pbkdf2.generateSalt();
        byte[] originalHash = pbkdf2.calculateHash(skf, password, salt);
        boolean hashMatches = pbkdf2.verifyPassword(skf, originalHash, password, salt);

        Assertions.assertAll(
                () -> assertNotNull(skf),
                () -> assertNotNull(salt),
                () -> assertNotNull(originalHash),
                () -> assertTrue(hashMatches)
        );
    }

    @Test
    void givenNotIdenticalPasswordsWhenComparingHashesReturnsFalse() throws Exception {
        char[] password = "TotallySecurePassword12345".toCharArray();

        SecretKeyFactory skf = pbkdf2.createSecretKeyFactory();
        byte[] salt = pbkdf2.generateSalt();
        byte[] originalHash = pbkdf2.calculateHash(skf, password, salt);
        boolean hashMatches = pbkdf2.verifyPassword(skf, originalHash, "fakePassword12345".toCharArray(), salt);

        Assertions.assertAll(
                () -> assertNotNull(skf),
                () -> assertNotNull(salt),
                () -> assertNotNull(originalHash),
                () -> assertFalse(hashMatches)
        );
    }
}