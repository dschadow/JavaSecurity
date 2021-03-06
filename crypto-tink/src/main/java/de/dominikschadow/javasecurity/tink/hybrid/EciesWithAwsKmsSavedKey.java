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
import com.google.crypto.tink.hybrid.EciesAeadHkdfPrivateKeyManager;
import com.google.crypto.tink.hybrid.HybridConfig;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

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

    /**
     * Init AeadConfig in the Tink library.
     */
    public EciesWithAwsKmsSavedKey() {
        try {
            HybridConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    /**
     * Stores the encrypted keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     * @param keyset
     */
    public void generateAndStorePrivateKey(File keyset, String awsMasterKeyUri) throws IOException, GeneralSecurityException {
        if (!keyset.exists()) {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(EciesAeadHkdfPrivateKeyManager.eciesP256HkdfHmacSha256Aes128GcmTemplate());
            keysetHandle.write(JsonKeysetWriter.withFile(keyset), new AwsKmsClient().withDefaultCredentials().getAead(awsMasterKeyUri));
        }
    }

    public KeysetHandle loadPrivateKey(File keyset, String awsMasterKeyUri) throws IOException, GeneralSecurityException {
        return KeysetHandle.read(JsonKeysetReader.withFile(keyset),
                new AwsKmsClient().withDefaultCredentials().getAead(awsMasterKeyUri));
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
