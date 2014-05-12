/*
 * Copyright (C) 2014 Dominik Schadow, dominikschadow@gmail.com
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
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * SHA512 hashing sample with plain Java. Uses a salt, configures the number of iterations and calculates the hash value.
 * <p>
 * Uses Google Guava to Base64 print the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class SHA512HashSample {
    private static final Logger logger = LoggerFactory.getLogger(SHA512HashSample.class);
    private static final String ALGORITHM = "SHA512";
    private static final int ITERATIONS = 1000000;
    private static final int SALT_SIZE = 64;

    public static void main(String[] args) {
        SHA512HashSample hs = new SHA512HashSample();
        String password = "Java Forum Stuttgart 2014";

        try {
            byte[] salt = hs.generateSalt();
            byte[] hash = hs.calculateHash(password, salt);
            boolean correct = hs.verifyPassword(hash, password, salt);

            logger.info("Entered password is correct: {}", correct);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        return salt;
    }

    private byte[] calculateHash(String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.reset();
        md.update(Bytes.concat(password.getBytes("UTF-8"), salt));
        byte[] hash = md.digest();

        logger.info("Hash algorithm {}, iterations {}, salt {}", ALGORITHM, ITERATIONS, BaseEncoding.base64().encode(salt));
        logger.info("1 hash round: {}", BaseEncoding.base64().encode(hash));

        for (int i = 0; i < ITERATIONS; i++) {
            md.reset();
            hash = md.digest(hash);
        }

        return hash;
    }

    private boolean verifyPassword(byte[] originalHash, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] comparisonHash = calculateHash(password, salt);

        logger.info("password {}", password);
        logger.info("hash 1: {}", BaseEncoding.base64().encode(originalHash));
        logger.info("hash 2: {}", BaseEncoding.base64().encode(comparisonHash));

        return Arrays.equals(originalHash, comparisonHash);
    }
}
