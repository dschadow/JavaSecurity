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

import com.google.crypto.tink.*;
import com.google.crypto.tink.signature.PublicKeySignFactory;
import com.google.crypto.tink.signature.PublicKeyVerifyFactory;
import com.google.crypto.tink.signature.SignatureConfig;
import com.google.crypto.tink.signature.SignatureKeyTemplates;
import de.dominikschadow.javasecurity.tink.TinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the PublicKeySign primitive. The used key is stored and loaded from the
 * project. Selected algorithm is ECDSA P256.
 *
 * @author Dominik Schadow
 */
public class EcdsaWithSavedKey {
    private static final Logger log = LoggerFactory.getLogger(EcdsaWithSavedKey.class);
    private static final String INITIAL_TEXT = "Some dummy text to work with";
    private static final String PRIVATE_KEYSET_FILENAME = "crypto-tink/src/main/resources/keysets/signature-ecdsa-private.json";
    private static final String PUBLIC_KEYSET_FILENAME = "crypto-tink/src/main/resources/keysets/signature-ecdsa-public.json";

    /**
     * Init SignatureConfig in the Tink library.
     */
    private EcdsaWithSavedKey() {
        try {
            SignatureConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    public static void main(String[] args) {
        EcdsaWithSavedKey demo = new EcdsaWithSavedKey();

        try {
            demo.generateAndStorePrivateKey();
            KeysetHandle privateKeysetHandle = demo.loadPrivateKey();

            demo.generateAndStorePublicKey(privateKeysetHandle);
            KeysetHandle publicKeysetHandle = demo.loadPublicKey();

            byte[] signature = demo.sign(privateKeysetHandle);
            boolean valid = demo.verify(publicKeysetHandle, signature);

            TinkUtils.printSignatureData(privateKeysetHandle, publicKeysetHandle, INITIAL_TEXT, signature, valid);
        } catch (GeneralSecurityException ex) {
            log.error("Failure during Tink usage", ex);
        } catch (IOException ex) {
            log.error("Failure during storing key", ex);
        }
    }

    /**
     * Stores the private keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    private void generateAndStorePrivateKey() throws IOException, GeneralSecurityException {
        File keysetFile = new File(PRIVATE_KEYSET_FILENAME);

        if (!keysetFile.exists()) {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(SignatureKeyTemplates.ECDSA_P256);
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keysetFile));
        }
    }

    private KeysetHandle loadPrivateKey() throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File(PRIVATE_KEYSET_FILENAME)));
    }

    /**
     * Stores the public keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    private void generateAndStorePublicKey(KeysetHandle privateKeysetHandle) throws IOException, GeneralSecurityException {
        File keysetFile = new File(PUBLIC_KEYSET_FILENAME);

        if (!keysetFile.exists()) {
            KeysetHandle keysetHandle = privateKeysetHandle.getPublicKeysetHandle();
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keysetFile));
        }
    }

    private KeysetHandle loadPublicKey() throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File(PUBLIC_KEYSET_FILENAME)));
    }

    private byte[] sign(KeysetHandle privateKeysetHandle) throws GeneralSecurityException {
        PublicKeySign signer = PublicKeySignFactory.getPrimitive(privateKeysetHandle);

        return signer.sign(INITIAL_TEXT.getBytes());
    }

    private boolean verify(KeysetHandle publicKeysetHandle, byte[] signature) {
        try {
            PublicKeyVerify verifier = PublicKeyVerifyFactory.getPrimitive(publicKeysetHandle);
            verifier.verify(signature, INITIAL_TEXT.getBytes());
            return true;
        } catch (GeneralSecurityException ex) {
            log.error("Signature is invalid", ex);
        }

        return false;
    }
}
