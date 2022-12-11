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

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the Authenticated Encryption with Associated Data (AEAD) primitive. The used
 * key is stored and loaded from the project. Selected algorithm is AES-GCM with 128 bit.
 *
 * @author Dominik Schadow
 */
public class AesGcmWithSavedKey {
    /**
     * Init AeadConfig in the Tink library.
     */
    public AesGcmWithSavedKey() throws GeneralSecurityException {
        AeadConfig.register();
    }

    /**
     * Stores the keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    public void generateAndStoreKey(File keyset) throws IOException, GeneralSecurityException {
        if (!keyset.exists()) {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES128_GCM"));
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keyset));
        }
    }

    public KeysetHandle loadKey(File keyset) throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(keyset));
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
