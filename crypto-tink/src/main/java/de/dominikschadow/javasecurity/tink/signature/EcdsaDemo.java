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
package de.dominikschadow.javasecurity.tink.signature;

import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.PublicKeySign;
import com.google.crypto.tink.PublicKeyVerify;
import com.google.crypto.tink.signature.PublicKeySignFactory;
import com.google.crypto.tink.signature.PublicKeyVerifyFactory;
import com.google.crypto.tink.signature.SignatureConfig;
import com.google.crypto.tink.signature.SignatureKeyTemplates;
import de.dominikschadow.javasecurity.tink.TinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the PublicKeySign primitive. The used key is generated during runtime and not
 * saved.
 *
 * @author Dominik Schadow
 */
public class EcdsaDemo {
    private static final Logger log = LoggerFactory.getLogger(EcdsaDemo.class);
    private static final String INITIAL_TEXT = "Some dummy text to work with";

    /**
     * Init SignatureConfig in the Tink library.
     */
    private EcdsaDemo() {
        try {
            SignatureConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    public static void main(String[] args) {
        EcdsaDemo demo = new EcdsaDemo();

        try {
            KeysetHandle privateKeysetHandle = demo.generatePrivateKey();
            KeysetHandle publicKeysetHandle = demo.generatePublicKey(privateKeysetHandle);

            byte[] signature = demo.sign(privateKeysetHandle);
            boolean valid = demo.verify(publicKeysetHandle, signature);

            TinkUtils.printSignatureData(privateKeysetHandle, publicKeysetHandle, INITIAL_TEXT, signature, valid);
        } catch (GeneralSecurityException ex) {
            log.error("Failure during Tink usage", ex);
        }
    }

    private KeysetHandle generatePrivateKey() throws GeneralSecurityException {
        return KeysetHandle.generateNew(SignatureKeyTemplates.ECDSA_P256);
    }

    private KeysetHandle generatePublicKey(KeysetHandle privateKeysetHandle) throws GeneralSecurityException {
        return privateKeysetHandle.getPublicKeysetHandle();
    }

    private byte[] sign(KeysetHandle publicKeysetHandle) throws GeneralSecurityException {
        PublicKeySign signer = PublicKeySignFactory.getPrimitive(publicKeysetHandle);

        return signer.sign(INITIAL_TEXT.getBytes());
    }

    private boolean verify(KeysetHandle privateKeysetHandle, byte[] signature) {
        try {
            PublicKeyVerify verifier = PublicKeyVerifyFactory.getPrimitive(privateKeysetHandle);
            verifier.verify(signature, INITIAL_TEXT.getBytes());
            return true;
        } catch (GeneralSecurityException ex) {
            log.error("Signature is invalid", ex);
        }

        return false;
    }

}
