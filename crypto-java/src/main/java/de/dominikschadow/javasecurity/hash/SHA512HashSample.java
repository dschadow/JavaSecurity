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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static void main(String[] args) {
        SHA512HashSample hs = new SHA512HashSample();
        String initialText = "Java Forum Stuttgart 2014";
        String salt = "BEQ7M947peq2UtviNYow";

        try {
            byte[] hash = hs.calculateHash(initialText, salt);
            boolean correct = hs.verifyPassword(hash, initialText, salt);

            logger.info("Entered password is correct: {}", correct);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private byte[] calculateHash(String initialText, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.reset();
        md.update((initialText + salt).getBytes("UTF-8"));

        byte[] hash = md.digest();

        logger.info("Hash algorithm {}, iterations {}, salt {}", ALGORITHM, ITERATIONS, salt);

        return hash;
    }

    private boolean verifyPassword(byte[] originalHash, String initialText, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.reset();
        md.update((initialText + salt).getBytes("UTF-8"));

        byte[] comparisonHash = md.digest();

        logger.info("initialText {}", initialText);
        logger.info("1 hash: {}", BaseEncoding.base64().encode(originalHash));
        logger.info("2 hash: {}", BaseEncoding.base64().encode(comparisonHash));

        return Arrays.equals(originalHash, comparisonHash);
    }
}
