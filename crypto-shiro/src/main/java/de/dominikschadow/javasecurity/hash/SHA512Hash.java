/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
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

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * SHA512 hashing sample with Apache Shiro. Uses a private base salt, configures the number of iterations, automatically
 * generates a salt and calculates the hash value.
 *
 * @author Dominik Schadow
 */
public class SHA512Hash {
    private static final Logger log = LoggerFactory.getLogger(SHA512Hash.class);
    /**
     * Nothing up my sleeve number as private salt, not good for production.
     */
    private static final byte[] PRIVATE_SALT_BYTES = {3, 1, 4, 1, 5, 9, 2, 6, 5};
    private static final int ITERATIONS = 1000000;

    /**
     * Private constructor.
     */
    private SHA512Hash() {
    }

    public static void main(String[] args) {
        String password = "SHA-512 hash sample text";

        Hash hash = calculateHash(password);
        boolean correct = verifyPassword(hash.getBytes(), hash.getSalt(), password);

        log.info("Entered password is correct: {}", correct);
    }

    private static Hash calculateHash(String password) {
        ByteSource privateSalt = ByteSource.Util.bytes(PRIVATE_SALT_BYTES);
        DefaultHashService hashService = new DefaultHashService();
        hashService.setPrivateSalt(privateSalt);
        hashService.setGeneratePublicSalt(true);
        hashService.setHashIterations(ITERATIONS);

        HashRequest.Builder builder = new HashRequest.Builder();
        builder.setSource(ByteSource.Util.bytes(password));

        Hash hash = hashService.computeHash(builder.build());

        log.info("Hash algorithm {}, iterations {}, public salt {}", hash.getAlgorithmName(), hash.getIterations(), hash.getSalt());

        return hash;
    }

    private static boolean verifyPassword(byte[] originalHash, ByteSource publicSalt, String password) {
        ByteSource privateSalt = ByteSource.Util.bytes(PRIVATE_SALT_BYTES);
        DefaultHashService hashService = new DefaultHashService();
        hashService.setPrivateSalt(privateSalt);
        hashService.setHashIterations(ITERATIONS);

        HashRequest.Builder builder = new HashRequest.Builder();
        builder.setSource(ByteSource.Util.bytes(password));
        builder.setSalt(publicSalt);

        Hash comparisonHash = hashService.computeHash(builder.build());

        log.info("password: {}", password);
        log.info("1 hash: {}", Hex.encodeToString(originalHash));
        log.info("2 hash: {}", comparisonHash.toHex());

        return Arrays.equals(originalHash, comparisonHash.getBytes());
    }
}
