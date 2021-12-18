/*
 * Copyright (C) 2021 Dominik Schadow, dominikschadow@gmail.com
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 hashing sample with plain Java. No salt and no iterations are used to calculate the hash value. This sample (and
 * the MD5 algorithm) is totally insecure.
 * <p/>
 * Uses Google Guava to hex encode the hash in a readable format.
 *
 * @author Dominik Schadow
 */
public class MD5 {
    private static final System.Logger LOG = System.getLogger(MD5.class.getName());
    private static final String ALGORITHM = "MD5";

    /**
     * Private constructor.
     */
    private MD5() {
    }

    public static void main(String[] args) {
        String password = "TotallySecurePassword12345";

        try {
            byte[] hash = calculateHash(password);
            boolean correct = verifyPassword(hash, password);

            LOG.log(System.Logger.Level.INFO, "Entered password is correct: {0}", correct);
        } catch (NoSuchAlgorithmException ex) {
            LOG.log(System.Logger.Level.ERROR, ex.getMessage(), ex);
        }
    }

    private static byte[] calculateHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.reset();
        md.update(password.getBytes(StandardCharsets.UTF_8));
        return md.digest();
    }

    private static boolean verifyPassword(byte[] originalHash, String password) throws NoSuchAlgorithmException {
        byte[] comparisonHash = calculateHash(password);

        LOG.log(System.Logger.Level.INFO, "hash 1: {0}", BaseEncoding.base16().encode(originalHash));
        LOG.log(System.Logger.Level.INFO, "hash 2: {0}", BaseEncoding.base16().encode(comparisonHash));

        return comparePasswords(originalHash, comparisonHash);
    }

    /**
     * Compares the two byte arrays in length-constant time using XOR.
     *
     * @param originalHash   The original password hash
     * @param comparisonHash The comparison password hash
     * @return True if both match, false otherwise
     */
    private static boolean comparePasswords(byte[] originalHash, byte[] comparisonHash) {
        int diff = originalHash.length ^ comparisonHash.length;
        for (int i = 0; i < originalHash.length && i < comparisonHash.length; i++) {
            diff |= originalHash[i] ^ comparisonHash[i];
        }

        return diff == 0;
    }
}
