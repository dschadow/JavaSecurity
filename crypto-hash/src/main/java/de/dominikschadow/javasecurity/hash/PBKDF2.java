/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
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

import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * PBKDF2 hashing sample with plain Java. Uses a salt, configures the number of iterations and calculates the hash
 * value.
 * <p/>
 * Uses Google Guava to Base64 print the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class PBKDF2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(PBKDF2.class);
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATIONS = 10000;
    // salt size at least 32 byte
    private static final int SALT_SIZE = 32;
    private static final int HASH_SIZE = 512;

    public static void main(String[] args) {
        PBKDF2 hs = new PBKDF2();
        char[] password = "TotallySecurePassword12345".toCharArray();

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] salt = hs.generateSalt();

            LOGGER.info("Password {}, hash algorithm {}, hash size {}, iterations {}, salt {}", String.valueOf
                    (password), ALGORITHM, HASH_SIZE, ITERATIONS, BaseEncoding.base64().encode(salt));

            byte[] hash = hs.calculateHash(skf, password, salt);
            boolean correct = hs.verifyPassword(skf, hash, password, salt);

            LOGGER.info("Entered password is correct: {}", correct);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        return salt;
    }

    private byte[] calculateHash(SecretKeyFactory skf, char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_SIZE);

        return skf.generateSecret(spec).getEncoded();
    }

    private boolean verifyPassword(SecretKeyFactory skf, byte[] originalHash, char[] password, byte[] salt) throws
            InvalidKeySpecException {
        byte[] comparisonHash = calculateHash(skf, password, salt);

        LOGGER.info("hash 1: {}", BaseEncoding.base64().encode(originalHash));
        LOGGER.info("hash 2: {}", BaseEncoding.base64().encode(comparisonHash));

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
