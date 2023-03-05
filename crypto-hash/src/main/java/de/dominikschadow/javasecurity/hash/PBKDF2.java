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

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import static de.dominikschadow.javasecurity.hash.PasswordComparator.comparePasswords;

/**
 * PBKDF2 hashing sample with plain Java. Uses a salt, configures the number of iterations and calculates the hash
 * value.
 *
 * @author Dominik Schadow
 */
public class PBKDF2 {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int ITERATIONS = 10000;
    // salt size at least 32 byte
    private static final int SALT_SIZE = 32;
    private static final int HASH_SIZE = 512;

    public SecretKeyFactory createSecretKeyFactory() throws NoSuchAlgorithmException {
        return SecretKeyFactory.getInstance(ALGORITHM);
    }

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        return salt;
    }

    public byte[] calculateHash(SecretKeyFactory skf, char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_SIZE);
        byte[] hash = skf.generateSecret(spec).getEncoded();
        spec.clearPassword();

        return hash;
    }

    public boolean verifyPassword(SecretKeyFactory skf, byte[] originalHash, char[] password, byte[] salt) throws
            InvalidKeySpecException {
        byte[] comparisonHash = calculateHash(skf, password, salt);

        return comparePasswords(originalHash, comparisonHash);
    }
}
