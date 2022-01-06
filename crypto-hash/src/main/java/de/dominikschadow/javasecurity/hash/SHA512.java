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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * SHA512 hashing sample with plain Java. Uses a salt, configures the number of iterations and calculates the hash
 * value.
 *
 * @author Dominik Schadow
 */
public class SHA512 {
    private static final String ALGORITHM = "SHA-512";
    private static final int ITERATIONS = 1000000;
    private static final int SALT_SIZE = 64;

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        return salt;
    }

    public byte[] calculateHash(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.reset();
        md.update(concatPasswordAndSalt(password.getBytes(StandardCharsets.UTF_8), salt));

        byte[] hash = md.digest();

        for (int i = 0; i < ITERATIONS; i++) {
            md.reset();
            hash = md.digest(hash);
        }

        return hash;
    }

    private byte[] concatPasswordAndSalt(byte[] password, byte[] salt) {
        byte[] passwordAndSalt = new byte[password.length + salt.length];
        System.arraycopy(password, 0, passwordAndSalt, 0, password.length);
        System.arraycopy(salt, 0, passwordAndSalt, password.length, salt.length);

        return passwordAndSalt;
    }

    public boolean verifyPassword(byte[] originalHash, String password, byte[] salt) throws
            NoSuchAlgorithmException {
        byte[] comparisonHash = calculateHash(password, salt);

        return comparePasswords(originalHash, comparisonHash);
    }

    /**
     * Compares the two byte arrays in length-constant time using XOR.
     *
     * @param originalHash   The original password hash
     * @param comparisonHash The comparison password hash
     * @return True if both match, false otherwise
     */
    private boolean comparePasswords(byte[] originalHash, byte[] comparisonHash) {
        int diff = originalHash.length ^ comparisonHash.length;
        for (int i = 0; i < originalHash.length && i < comparisonHash.length; i++) {
            diff |= originalHash[i] ^ comparisonHash[i];
        }

        return diff == 0;
    }
}
