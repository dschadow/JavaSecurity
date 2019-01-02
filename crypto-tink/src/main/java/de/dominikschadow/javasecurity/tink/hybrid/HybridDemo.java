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
package de.dominikschadow.javasecurity.tink.hybrid;

import com.google.crypto.tink.HybridDecrypt;
import com.google.crypto.tink.HybridEncrypt;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.hybrid.HybridConfig;
import com.google.crypto.tink.hybrid.HybridDecryptFactory;
import com.google.crypto.tink.hybrid.HybridEncryptFactory;
import com.google.crypto.tink.hybrid.HybridKeyTemplates;
import de.dominikschadow.javasecurity.tink.TinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the HybridEncrypt primitive. The used key is generated during runtime and not
 * saved.
 *
 * @author Dominik Schadow
 */
public class HybridDemo {
    private static final Logger log = LoggerFactory.getLogger(HybridDemo.class);
    private static final String INITIAL_TEXT = "Some dummy text to work with";
    private static final String ASSOCIATED_DATA = "Some additional data";

    /**
     * Init Hybrid in the Tink library.
     */
    private HybridDemo() {
        try {
            HybridConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    public static void main(String[] args) {
        HybridDemo demo = new HybridDemo();

        try {
            KeysetHandle privateKeysetHandle = demo.generatePrivateKey();
            KeysetHandle publicKeysetHandle = demo.generatePublicKey(privateKeysetHandle);

            byte[] cipherText = demo.encrypt(publicKeysetHandle);
            byte[] plainText = demo.decrypt(privateKeysetHandle, cipherText);

            TinkUtils.printHybridEncryptionData(privateKeysetHandle, publicKeysetHandle, INITIAL_TEXT, cipherText, plainText);
        } catch (GeneralSecurityException ex) {
            log.error("Failure during Tink usage", ex);
        }
    }

    private KeysetHandle generatePrivateKey() throws GeneralSecurityException {
        return KeysetHandle.generateNew(HybridKeyTemplates.ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM);
    }

    private KeysetHandle generatePublicKey(KeysetHandle privateKeysetHandle) throws GeneralSecurityException {
        return privateKeysetHandle.getPublicKeysetHandle();
    }

    private byte[] encrypt(KeysetHandle publicKeysetHandle) throws GeneralSecurityException {
        HybridEncrypt hybridEncrypt = HybridEncryptFactory.getPrimitive(publicKeysetHandle);

        return hybridEncrypt.encrypt(INITIAL_TEXT.getBytes(), ASSOCIATED_DATA.getBytes());
    }

    private byte[] decrypt(KeysetHandle privateKeysetHandle, byte[] cipherText) throws GeneralSecurityException {
        HybridDecrypt hybridDecrypt = HybridDecryptFactory.getPrimitive(privateKeysetHandle);

        return hybridDecrypt.decrypt(cipherText, ASSOCIATED_DATA.getBytes());
    }
}
