/*
 * Copyright (C) 2026 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.kdf;

import javax.crypto.KDF;
import javax.crypto.SecretKey;
import javax.crypto.spec.HKDFParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Key derivation sample with plain Java using the HMAC-based Extract-and-Expand Key Derivation Function (HKDF, RFC 5869)
 * via the Key Derivation Function API (JEP 510).
 * <p/>
 * HKDF derives one or more cryptographically strong keys from input key material that is already random, e.g. a shared
 * secret from ML-KEM or ECDH. Different {@code info} values produce independent keys for different purposes from the
 * same input key material. HKDF is <strong>not</strong> suitable for hashing passwords, use PBKDF2, bcrypt, scrypt or
 * Argon2 for that.
 *
 * @author Dominik Schadow
 */
public class HKDF {
    private static final String ALGORITHM = "HKDF-SHA256";
    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_SIZE_BYTES = 32;

    /**
     * Derives an AES-256 key from the given input key material.
     *
     * @param inputKeyMaterial The random input key material
     * @param salt             Optional (non-secret) salt, may be empty
     * @param info             Context information binding the derived key to its purpose
     * @return The derived AES key
     */
    public SecretKey deriveKey(byte[] inputKeyMaterial, byte[] salt, byte[] info) throws NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {
        KDF hkdf = KDF.getInstance(ALGORITHM);
        return hkdf.deriveKey(KEY_ALGORITHM, createParameters(inputKeyMaterial, salt, info, KEY_SIZE_BYTES));
    }

    /**
     * Derives the given number of bytes from the given input key material.
     *
     * @param inputKeyMaterial The random input key material
     * @param salt             Optional (non-secret) salt, may be empty
     * @param info             Context information binding the derived data to its purpose
     * @param length           Number of bytes to derive
     * @return The derived bytes
     */
    public byte[] deriveData(byte[] inputKeyMaterial, byte[] salt, byte[] info, int length) throws
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KDF hkdf = KDF.getInstance(ALGORITHM);
        return hkdf.deriveData(createParameters(inputKeyMaterial, salt, info, length));
    }

    private AlgorithmParameterSpec createParameters(byte[] inputKeyMaterial, byte[] salt, byte[] info, int length) {
        return HKDFParameterSpec.ofExtract()
                .addIKM(inputKeyMaterial)
                .addSalt(salt)
                .thenExpand(info, length);
    }
}
