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

import com.google.crypto.tink.*;
import com.google.crypto.tink.hybrid.HybridConfig;
import com.google.crypto.tink.hybrid.HybridKeyTemplates;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import de.dominikschadow.javasecurity.tink.TinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static de.dominikschadow.javasecurity.tink.TinkUtils.AWS_MASTER_KEY_URI;

/**
 * Shows crypto usage with Google Tink for the HybridEncrypt primitive. The used key is stored and loaded from AWS KMS.
 * Requires a master key available in AWS KMS and correctly configured credentials to access AWS KMS: AWS_ACCESS_KEY_ID
 * and AWS_SECRET_KEY must be set as environment variables.
 * <p>
 * Selected algorithm is ECIES with AEAD and HKDF.
 *
 * @author Dominik Schadow
 * @see <a href="https://docs.aws.amazon.com/kms/latest/developerguide/create-keys.html">Creating Keys</a>
 * @see <a href="https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html#credentials-default">Using
 * the Default Credential Provider Chain</a>
 */
public class EciesWithAwsKmsSavedKey {
    private static final Logger log = LoggerFactory.getLogger(EciesWithAwsKmsSavedKey.class);
    private static final String INITIAL_TEXT = "Some dummy text to work with";
    private static final String CONTEXT_INFO = "Some additional data";
    private static final String PRIVATE_KEYSET_FILENAME = "crypto-tink/src/main/resources/keysets/hybrid-ecies-kms-private.json";
    private static final String PUBLIC_KEYSET_FILENAME = "crypto-tink/src/main/resources/keysets/hybrid-ecies-kms-public.json";

    /**
     * Init AeadConfig in the Tink library.
     */
    private EciesWithAwsKmsSavedKey() {
        try {
            HybridConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    public static void main(String[] args) {
        EciesWithAwsKmsSavedKey demo = new EciesWithAwsKmsSavedKey();

        try {
            demo.generateAndStorePrivateKey();
            KeysetHandle privateKeysetHandle = demo.loadPrivateKey();

            demo.generateAndStorePublicKey(privateKeysetHandle);
            KeysetHandle publicKeysetHandle = demo.loadPublicKey();

            byte[] cipherText = demo.encrypt(publicKeysetHandle);
            byte[] plainText = demo.decrypt(privateKeysetHandle, cipherText);

            TinkUtils.printHybridEncryptionData(privateKeysetHandle, publicKeysetHandle, INITIAL_TEXT, cipherText, plainText);
        } catch (GeneralSecurityException ex) {
            log.error("Failure during Tink usage", ex);
        } catch (IOException ex) {
            log.error("Failure during storing key", ex);
        }
    }

    /**
     * Stores the encrypted keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    private void generateAndStorePrivateKey() throws IOException, GeneralSecurityException {
        File keysetFile = new File(PRIVATE_KEYSET_FILENAME);

        if (!keysetFile.exists()) {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(HybridKeyTemplates.ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM);
            keysetHandle.write(JsonKeysetWriter.withFile(keysetFile), new AwsKmsClient().withDefaultCredentials().getAead(AWS_MASTER_KEY_URI));
        }
    }

    private KeysetHandle loadPrivateKey() throws IOException, GeneralSecurityException {
        return KeysetHandle.read(JsonKeysetReader.withFile(new File(PRIVATE_KEYSET_FILENAME)),
                new AwsKmsClient().withDefaultCredentials().getAead(AWS_MASTER_KEY_URI));
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

    private byte[] encrypt(KeysetHandle publicKeysetHandle) throws GeneralSecurityException {
        HybridEncrypt hybridEncrypt = publicKeysetHandle.getPrimitive(HybridEncrypt.class);

        return hybridEncrypt.encrypt(INITIAL_TEXT.getBytes(), CONTEXT_INFO.getBytes());
    }

    private byte[] decrypt(KeysetHandle privateKeysetHandle, byte[] cipherText) throws GeneralSecurityException {
        HybridDecrypt hybridDecrypt = privateKeysetHandle.getPrimitive(HybridDecrypt.class);

        return hybridDecrypt.decrypt(cipherText, CONTEXT_INFO.getBytes());
    }
}
