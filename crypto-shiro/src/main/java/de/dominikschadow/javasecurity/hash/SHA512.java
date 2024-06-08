/*
 * Copyright (C) 2023 Dominik Schadow, dominikschadow@gmail.com
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

import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.lang.util.ByteSource;

import java.util.Arrays;

/**
 * SHA512 hashing sample with Apache Shiro. Uses a private base salt, configures the number of iterations, automatically
 * generates a salt and calculates the hash value.
 *
 * @author Dominik Schadow
 */
public class SHA512 {
    /**
     * Nothing up my sleeve number as private salt, not good for production.
     */
    private static final byte[] PRIVATE_SALT_BYTES = {3, 1, 4, 1, 5, 9, 2, 6, 5};

    public Hash calculateHash(String password) {
        ByteSource privateSalt = ByteSource.Util.bytes(PRIVATE_SALT_BYTES);
        DefaultHashService hashService = new DefaultHashService();

        HashRequest.Builder builder = new HashRequest.Builder();
        builder.setSource(ByteSource.Util.bytes(password));
        builder.setSalt(privateSalt);
        builder.setAlgorithmName("SHA-512");

        return hashService.computeHash(builder.build());
    }

    public boolean verifyPassword(byte[] originalHash, ByteSource publicSalt, String password) {
        DefaultHashService hashService = new DefaultHashService();

        HashRequest.Builder builder = new HashRequest.Builder();
        builder.setSource(ByteSource.Util.bytes(password));
        builder.setSalt(publicSalt);
        builder.setAlgorithmName("SHA-512");

        Hash comparisonHash = hashService.computeHash(builder.build());

        return Arrays.equals(originalHash, comparisonHash.getBytes());
    }
}
