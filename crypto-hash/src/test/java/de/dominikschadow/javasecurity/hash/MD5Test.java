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

import com.google.common.hash.HashCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MD5Test {
    private final MD5 md5 = new MD5();

    @Test
    void givenIdenticalPasswordsWhenComparingHashesReturnsTrue() throws Exception {
        String password = "TotallySecurePassword12345";

        byte[] originalHash = md5.calculateHash(password);
        boolean hashMatches = md5.verifyPassword(originalHash, password);

        Assertions.assertAll(
                () -> assertEquals("6ee66e42a8e60d5fb816030b188c4c79", HashCode.fromBytes(originalHash).toString()),
                () -> assertTrue(hashMatches)
        );
    }

    @Test
    void givenNotIdenticalPasswordsWhenComparingHashesReturnsFalse() throws Exception {
        String password = "TotallySecurePassword12345";

        byte[] originalHash = md5.calculateHash(password);
        boolean hashMatches = md5.verifyPassword(originalHash, "fakePassword12345");

        Assertions.assertAll(
                () -> assertEquals("6ee66e42a8e60d5fb816030b188c4c79", HashCode.fromBytes(originalHash).toString()),
                () -> assertFalse(hashMatches)
        );
    }
}