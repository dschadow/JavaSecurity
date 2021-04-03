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
package de.dominikschadow.javasecurity.tink.signature;

import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.PublicKeySign;
import com.google.crypto.tink.PublicKeyVerify;
import com.google.crypto.tink.signature.EcdsaSignKeyManager;
import com.google.crypto.tink.signature.SignatureConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the PublicKeySign primitive. The used key is generated during runtime and not
 * saved. Selected algorithm is ECDSA P384.
 *
 * @author Dominik Schadow
 */
public class EcdsaWithGeneratedKey {
    private static final Logger log = LoggerFactory.getLogger(EcdsaWithGeneratedKey.class);

    /**
     * Init SignatureConfig in the Tink library.
     */
    public EcdsaWithGeneratedKey() {
        try {
            SignatureConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    public KeysetHandle generatePrivateKey() throws GeneralSecurityException {
        return KeysetHandle.generateNew(EcdsaSignKeyManager.ecdsaP256Template());
    }

    public KeysetHandle generatePublicKey(KeysetHandle privateKeysetHandle) throws GeneralSecurityException {
        return privateKeysetHandle.getPublicKeysetHandle();
    }

    public byte[] sign(KeysetHandle privateKeysetHandle, byte[] initialText) throws GeneralSecurityException {
        PublicKeySign signer = privateKeysetHandle.getPrimitive(PublicKeySign.class);

        return signer.sign(initialText);
    }

    public boolean verify(KeysetHandle publicKeysetHandle, byte[] signature, byte[] initialText) {
        try {
            PublicKeyVerify verifier = publicKeysetHandle.getPrimitive(PublicKeyVerify.class);
            verifier.verify(signature, initialText);
            return true;
        } catch (GeneralSecurityException ex) {
            log.error("Signature is invalid", ex);
        }

        return false;
    }
}
