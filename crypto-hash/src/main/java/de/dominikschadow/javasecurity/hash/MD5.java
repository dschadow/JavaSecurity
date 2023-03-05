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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static de.dominikschadow.javasecurity.hash.PasswordComparator.comparePasswords;

/**
 * MD5 hashing sample with plain Java. No salt and no iterations are used to calculate the hash value. This sample (and
 * the MD5 algorithm) is totally insecure.
 *
 * @author Dominik Schadow
 */
public class MD5 {
    private static final String ALGORITHM = "MD5";

    public byte[] calculateHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.reset();
        md.update(password.getBytes(StandardCharsets.UTF_8));
        return md.digest();
    }

    public boolean verifyPassword(byte[] originalHash, String password) throws NoSuchAlgorithmException {
        byte[] comparisonHash = calculateHash(password);

        return comparePasswords(originalHash, comparisonHash);
    }
}
