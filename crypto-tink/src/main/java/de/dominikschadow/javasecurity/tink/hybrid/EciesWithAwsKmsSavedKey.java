/*
 * Copyright (C) 2023 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
import com.google.crypto.tink.integration.awskms.AwsKmsClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * <p>
 * Shows crypto usage with Google Tink for the HybridEncrypt (AEAD) primitive. The used key is stored and loaded from #
 * AWS KMS. Selected algorithm is AES-GCM with 128 bit. Requires a master key available in AWS KMS and correctly
 * configured credentials to access AWS KMS: AWS_ACCESS_KEY_ID and AWS_SECRET_KEY must be set as environment variables.
 * </p>
 * <p>
 * Using your own AWS Master Key requires to delete the stored keyset in src/test/resources/keysets/hybrid-ecies-kms-private.json
 * and src/test/resources/keysets/hybrid-ecies-kms-public.json because these keys were created with the used sample AWS
 * KMS master key and will not work with any other master key.
 * </p>
 *
 * @author Dominik Schadow
 * @see <a href="https://docs.aws.amazon.com/kms/latest/developerguide/create-keys.html">Creating Keys</a>
 * @see <a href="https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html#credentials-default">Using
 * the Default Credential Provider Chain</a>
 */
public class EciesWithAwsKmsSavedKey {
    private static final String AWS_MASTER_KEY_URI = "aws-kms://arn:aws:kms:us-east-1:776241929911:key/7aeb00c6-d416-4130-bed1-a8ee6064d7d9";
    private final AwsKmsClient awsKmsClient = new AwsKmsClient();

    /**
     * Init HybridConfig in the Tink library.
     */
    public EciesWithAwsKmsSavedKey() throws GeneralSecurityException {
        HybridConfig.register();
    }

    /**
     * Stores the encrypted keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    public void generateAndStorePrivateKey(File keyset) throws IOException, GeneralSecurityException {
        if (!keyset.exists()) {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM"));
            keysetHandle.write(JsonKeysetWriter.withOutputStream(new FileOutputStream((keyset))), awsKmsClient.getAead(AWS_MASTER_KEY_URI));
        }
    }

    public KeysetHandle loadPrivateKey(File keyset) throws IOException, GeneralSecurityException {
        return KeysetHandle.read(JsonKeysetReader.withInputStream(new FileInputStream(keyset)), awsKmsClient.getAead(AWS_MASTER_KEY_URI));
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
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withOutputStream(new FileOutputStream((keyset))));
        }
    }

    public KeysetHandle loadPublicKey(File keyset) throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withInputStream(new FileInputStream(keyset)));
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
