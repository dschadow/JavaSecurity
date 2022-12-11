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
package de.dominikschadow.javasecurity.tink.hybrid;

import com.google.crypto.tink.*;
import com.google.crypto.tink.hybrid.HybridConfig;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the HybridEncrypt primitive. The used key is stored and loaded from the
 * project. Selected algorithm is ECIES with AEAD and HKDF.
 *
 * @author Dominik Schadow
 */
public class EciesWithSavedKey {
    /**
     * Init HybridConfig in the Tink library.
     */
    public EciesWithSavedKey() throws GeneralSecurityException {
        HybridConfig.register();
    }

    /**
     * Stores the private keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    public void generateAndStorePrivateKey(File keyset) throws IOException, GeneralSecurityException {
        if (!keyset.exists()) {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM"));
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keyset));
        }
    }

    public KeysetHandle loadPrivateKey(File keyset) throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(keyset));
    }

    /**
     * Stores the public keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    public void generateAndStorePublicKey(KeysetHandle privateKeysetHandle, File keyset) throws IOException, GeneralSecurityException {
        if (!keyset.exists()) {
            KeysetHandle keysetHandle = privateKeysetHandle.getPublicKeysetHandle();
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keyset));
        }
    }

    public KeysetHandle loadPublicKey(File keyset) throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(keyset));
    }

    public byte[] encrypt(KeysetHandle publicKeysetHandle, byte[] initialText, byte[] contextInfo) throws GeneralSecurityException {
        HybridEncrypt hybridEncrypt = publicKeysetHandle.getPrimitive(HybridEncrypt.class);

        return hybridEncrypt.encrypt(initialText, contextInfo);
    }

    public byte[] decrypt(KeysetHandle privateKeysetHandle, byte[] cipherText, byte[] contextInfo) throws GeneralSecurityException {
        HybridDecrypt hybridDecrypt = privateKeysetHandle.getPrimitive(HybridDecrypt.class);

        return hybridDecrypt.decrypt(cipherText, contextInfo);
    }
}
