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

import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the HybridEncrypt primitive. The used key is generated and rotated during
 * runtime and not saved. Selected algorithm is ECIES with AEAD and HKDF.
 *
 * @author Dominik Schadow
 */
public class EciesWithGeneratedKeyAndKeyRotation {
    /**
     * Init HybridConfig in the Tink library.
     */
    public EciesWithGeneratedKeyAndKeyRotation() throws GeneralSecurityException {
        HybridConfig.register();
    }

    /**
     * Generate a new key with different ECIES properties and add it to the keyset. Sets the new key as primary key and
     * disables the original primary key.
     */
    public KeysetHandle rotateKey(KeysetHandle keysetHandle) throws GeneralSecurityException {
        KeysetHandle handle = KeysetManager.withKeysetHandle(keysetHandle).add(KeyTemplates.get("ECIES_P256_HKDF_HMAC_SHA256_AES128_CTR_HMAC_SHA256")).getKeysetHandle();

        handle = KeysetManager.withKeysetHandle(handle).setPrimary(handle.getKeysetInfo().getKeyInfo(1).getKeyId()).getKeysetHandle();

        return KeysetManager.withKeysetHandle(handle).disable(handle.getKeysetInfo().getKeyInfo(0).getKeyId()).getKeysetHandle();
    }

    public KeysetHandle generatePrivateKey() throws GeneralSecurityException {
        return KeysetHandle.generateNew(KeyTemplates.get("ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM"));
    }

    public KeysetHandle generatePublicKey(KeysetHandle privateKeysetHandle) throws GeneralSecurityException {
        return privateKeysetHandle.getPublicKeysetHandle();
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
