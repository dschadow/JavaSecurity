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
package de.dominikschadow.javasecurity.tink.aead;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;

import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the Authenticated Encryption with Associated Data (AEAD) primitive. The used
 * key is generated during runtime and not saved. Selected algorithm is AES-EAX with 256 bit.
 *
 * @author Dominik Schadow
 */
public class AesEaxWithGeneratedKey {
    /**
     * Init AeadConfig in the Tink library.
     */
    public AesEaxWithGeneratedKey() throws GeneralSecurityException {
        AeadConfig.register();
    }

    public KeysetHandle generateKey() throws GeneralSecurityException {
        return KeysetHandle.generateNew(KeyTemplates.get("AES256_EAX"));
    }

    public byte[] encrypt(KeysetHandle keysetHandle, byte[] initialText, byte[] associatedData) throws GeneralSecurityException {
        Aead aead = keysetHandle.getPrimitive(Aead.class);

        return aead.encrypt(initialText, associatedData);
    }

    public byte[] decrypt(KeysetHandle keysetHandle, byte[] cipherText, byte[] associatedData) throws GeneralSecurityException {
        Aead aead = keysetHandle.getPrimitive(Aead.class);

        return aead.decrypt(cipherText, associatedData);
    }
}
