/*
 * Copyright (C) 2019 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.tink.aead;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import de.dominikschadow.javasecurity.tink.TinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the Authenticated Encryption with Associated Data (AEAD) primitive. The used
 * key is generated during runtime and not saved. Selected algorithm is AES-EAX with 256 bit.
 *
 * @author Dominik Schadow
 */
public class AesEaxWithGeneratedKey {
    private static final Logger log = LoggerFactory.getLogger(AesEaxWithGeneratedKey.class);
    private static final String INITIAL_TEXT = "Some dummy text to work with";
    private static final String ASSOCIATED_DATA = "Some additional data";

    /**
     * Init AeadConfig in the Tink library.
     */
    private AesEaxWithGeneratedKey() {
        try {
            AeadConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    public static void main(String[] args) {
        AesEaxWithGeneratedKey demo = new AesEaxWithGeneratedKey();

        try {
            KeysetHandle keysetHandle = demo.generateKey();

            byte[] cipherText = demo.encrypt(keysetHandle);
            byte[] plainText = demo.decrypt(keysetHandle, cipherText);

            TinkUtils.printSymmetricEncryptionData(keysetHandle, INITIAL_TEXT, cipherText, plainText);
        } catch (GeneralSecurityException ex) {
            log.error("Failure during Tink usage", ex);
        }
    }

    private KeysetHandle generateKey() throws GeneralSecurityException {
        return KeysetHandle.generateNew(AeadKeyTemplates.AES256_EAX);
    }

    private byte[] encrypt(KeysetHandle keysetHandle) throws GeneralSecurityException {
        Aead aead = AeadFactory.getPrimitive(keysetHandle);

        return aead.encrypt(INITIAL_TEXT.getBytes(), ASSOCIATED_DATA.getBytes());
    }

    private byte[] decrypt(KeysetHandle keysetHandle, byte[] cipherText) throws GeneralSecurityException {
        Aead aead = AeadFactory.getPrimitive(keysetHandle);

        return aead.decrypt(cipherText, ASSOCIATED_DATA.getBytes());
    }
}
