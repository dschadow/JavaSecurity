/*
 * Copyright (C) 2021 Dominik Schadow, dominikschadow@gmail.com
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

import com.google.crypto.tink.HybridDecrypt;
import com.google.crypto.tink.HybridEncrypt;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.KeysetManager;
import com.google.crypto.tink.hybrid.HybridConfig;
import com.google.crypto.tink.hybrid.HybridKeyTemplates;
import de.dominikschadow.javasecurity.tink.TinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the HybridEncrypt primitive. The used key is generated and rotated during
 * runtime and not saved. Selected algorithm is ECIES with AEAD and HKDF.
 *
 * @author Dominik Schadow
 */
public class EciesWithGeneratedKeyAndKeyRotation {
    private static final Logger log = LoggerFactory.getLogger(EciesWithGeneratedKeyAndKeyRotation.class);
    private static final String INITIAL_TEXT = "Some dummy text to work with";
    private static final String CONTEXT_INFO = "Some additional data";

    /**
     * Init HybridConfig in the Tink library.
     */
    private EciesWithGeneratedKeyAndKeyRotation() {
        try {
            HybridConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    public static void main(String[] args) {
        EciesWithGeneratedKeyAndKeyRotation demo = new EciesWithGeneratedKeyAndKeyRotation();

        try {
            KeysetHandle privateKeysetHandle = demo.generatePrivateKey();
            TinkUtils.printKeyset("original keyset data", privateKeysetHandle);
            KeysetHandle rotatedPrivateKeysetHandle = demo.rotateKey(privateKeysetHandle);
            TinkUtils.printKeyset("rotated keyset data", rotatedPrivateKeysetHandle);
            rotatedPrivateKeysetHandle = demo.disableOriginalKey(rotatedPrivateKeysetHandle);
            TinkUtils.printKeyset("disabled rotated keyset data", rotatedPrivateKeysetHandle);
            KeysetHandle publicKeysetHandle = demo.generatePublicKey(rotatedPrivateKeysetHandle);

            byte[] cipherText = demo.encrypt(publicKeysetHandle);
            byte[] plainText = demo.decrypt(rotatedPrivateKeysetHandle, cipherText);

            TinkUtils.printHybridEncryptionData(rotatedPrivateKeysetHandle, publicKeysetHandle, INITIAL_TEXT, cipherText, plainText);
        } catch (GeneralSecurityException ex) {
            log.error("Failure during Tink usage", ex);
        }
    }

    /**
     * Generate a new key with different ECIES properties and add it to the keyset.
     */
    private KeysetHandle rotateKey(KeysetHandle keysetHandle) throws GeneralSecurityException {
        return KeysetManager.withKeysetHandle(keysetHandle).rotate(HybridKeyTemplates.ECIES_P256_HKDF_HMAC_SHA256_AES128_CTR_HMAC_SHA256).getKeysetHandle();
    }

    /**
     * Optional step to disable the original key.
     */
    private KeysetHandle disableOriginalKey(KeysetHandle keysetHandle) throws GeneralSecurityException {
        return KeysetManager.withKeysetHandle(keysetHandle).disable(keysetHandle.getKeysetInfo().getKeyInfo(0).getKeyId()).getKeysetHandle();
    }

    private KeysetHandle generatePrivateKey() throws GeneralSecurityException {
        return KeysetHandle.generateNew(HybridKeyTemplates.ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM);
    }

    private KeysetHandle generatePublicKey(KeysetHandle privateKeysetHandle) throws GeneralSecurityException {
        return privateKeysetHandle.getPublicKeysetHandle();
    }

    private byte[] encrypt(KeysetHandle publicKeysetHandle) throws GeneralSecurityException {
        HybridEncrypt hybridEncrypt = publicKeysetHandle.getPrimitive(HybridEncrypt.class);

        return hybridEncrypt.encrypt(INITIAL_TEXT.getBytes(), CONTEXT_INFO.getBytes());
    }

    private byte[] decrypt(KeysetHandle privateKeysetHandle, byte[] cipherText) throws GeneralSecurityException {
        HybridDecrypt hybridDecrypt = privateKeysetHandle.getPrimitive(HybridDecrypt.class);

        return hybridDecrypt.decrypt(cipherText, CONTEXT_INFO.getBytes());
    }
}
