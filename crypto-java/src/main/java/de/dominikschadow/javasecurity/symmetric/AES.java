/*
 * Copyright (C) 2022 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.symmetric;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Symmetric encryption sample with plain Java. Loads the AES key from the sample keystore, encrypts and decrypts sample
 * text with it.
 * <p/>
 * Note that the {@code INITIALIZATION_VECTOR} is not stored. One possibility to store it is to prepend it to the
 * encrypted message with a delimiter (all in Base64 encoding): {@code Base64(IV) + DELIMITER + Base64(ENCRYPTED *
 * MESSAGE)}
 * <p/>
 * Uses Google Guava to hex the encrypted message as readable format.
 *
 * @author Dominik Schadow
 */
public class AES {
    private final SecretKeySpec secretKeySpec;
    private final Cipher cipher;

    public AES(SecretKeySpec secretKeySpec, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance(algorithm);

        this.secretKeySpec = secretKeySpec;
    }

    public byte[] encrypt(String initialText) throws
            BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return cipher.doFinal(initialText.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] decrypt(byte[] ciphertext) throws
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(cipher.getIV()));
        return cipher.doFinal(ciphertext);
    }
}
