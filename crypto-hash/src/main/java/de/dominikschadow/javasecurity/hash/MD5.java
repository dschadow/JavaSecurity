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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 hashing sample with plain Java. No salt and no iterations are used to calculate the hash
 * value. This sample (and the MD5 algorithm) is totally insecure.
 * <p/>
 * Uses Google Guava to hex encode the hash in a readable format.
 *
 * @author Dominik Schadow
 */
public class MD5 {
    private static final Logger LOGGER = LoggerFactory.getLogger(MD5.class);
    private static final String ALGORITHM = "MD5";

    public static void main(String[] args) {
        MD5 hs = new MD5();
        String password = "TotallySecurePassword12345";

        try {
            byte[] hash = hs.calculateHash(password);
            boolean correct = hs.verifyPassword(hash, password);

            LOGGER.info("Entered password is correct: {}", correct);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }


    private byte[] calculateHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.reset();
        md.update(password.getBytes("UTF-8"));
        return md.digest();
    }

    private boolean verifyPassword(byte[] originalHash, String password) throws
            NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] comparisonHash = calculateHash(password);

        LOGGER.info("hash 1: {}", BaseEncoding.base16().encode(originalHash));
        LOGGER.info("hash 2: {}", BaseEncoding.base16().encode(comparisonHash));

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
