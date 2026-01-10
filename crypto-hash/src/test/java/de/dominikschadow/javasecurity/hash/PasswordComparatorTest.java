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
package de.dominikschadow.javasecurity.hash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordComparatorTest {

    @Test
    void givenIdenticalHashesWhenComparingReturnsTrue() {
        byte[] originalHash = {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] comparisonHash = {0x01, 0x02, 0x03, 0x04, 0x05};

        boolean result = PasswordComparator.comparePasswords(originalHash, comparisonHash);

        assertTrue(result);
    }

    @Test
    void givenDifferentHashesWhenComparingReturnsFalse() {
        byte[] originalHash = {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] comparisonHash = {0x01, 0x02, 0x03, 0x04, 0x06};

        boolean result = PasswordComparator.comparePasswords(originalHash, comparisonHash);

        assertFalse(result);
    }

    @Test
    void givenDifferentLengthHashesWhenComparingReturnsFalse() {
        byte[] originalHash = {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] comparisonHash = {0x01, 0x02, 0x03};

        boolean result = PasswordComparator.comparePasswords(originalHash, comparisonHash);

        assertFalse(result);
    }

    @Test
    void givenEmptyHashesWhenComparingReturnsTrue() {
        byte[] originalHash = {};
        byte[] comparisonHash = {};

        boolean result = PasswordComparator.comparePasswords(originalHash, comparisonHash);

        assertTrue(result);
    }

    @Test
    void givenOneEmptyHashWhenComparingReturnsFalse() {
        byte[] originalHash = {0x01, 0x02, 0x03};
        byte[] comparisonHash = {};

        boolean result = PasswordComparator.comparePasswords(originalHash, comparisonHash);

        assertFalse(result);
    }

    @Test
    void givenCompletelyDifferentHashesWhenComparingReturnsFalse() {
        byte[] originalHash = {0x00, 0x00, 0x00, 0x00};
        byte[] comparisonHash = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

        boolean result = PasswordComparator.comparePasswords(originalHash, comparisonHash);

        assertFalse(result);
    }

    @Test
    void givenSingleByteIdenticalHashesWhenComparingReturnsTrue() {
        byte[] originalHash = {0x42};
        byte[] comparisonHash = {0x42};

        boolean result = PasswordComparator.comparePasswords(originalHash, comparisonHash);

        assertTrue(result);
    }

    @Test
    void givenSingleByteDifferentHashesWhenComparingReturnsFalse() {
        byte[] originalHash = {0x42};
        byte[] comparisonHash = {0x43};

        boolean result = PasswordComparator.comparePasswords(originalHash, comparisonHash);

        assertFalse(result);
    }
}
