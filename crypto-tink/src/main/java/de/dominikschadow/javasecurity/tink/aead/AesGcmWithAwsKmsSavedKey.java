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
package de.dominikschadow.javasecurity.tink.aead;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

/**
 * <p>
 * Shows crypto usage with Google Tink for the Authenticated Encryption with Associated Data (AEAD) primitive. The used
 * key is stored and loaded from AWS KMS. Selected algorithm is AES-GCM with 128 bit. Requires a master key available in
 * AWS KMS and correctly configured credentials to access AWS KMS: AWS_ACCESS_KEY_ID and AWS_SECRET_KEY must be set as
 * environment variables.
 * </p>
 * <p>
 * Using your own AWS Master Key requires to delete the stored keyset in src/test/resources/keysets/aead-aes-gcm-kms.json
 * because this key was created with the used sample AWS KMS master key and will not work with any other master key.
 * </p>
 *
 * @author Dominik Schadow
 * @see <a href="https://docs.aws.amazon.com/kms/latest/developerguide/create-keys.html">Creating Keys</a>
 * @see <a href="https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html#credentials-default">Using
 * the Default Credential Provider Chain</a>
 */
public class AesGcmWithAwsKmsSavedKey {
    private static final String AWS_MASTER_KEY_URI = "aws-kms://arn:aws:kms:eu-central-1:776241929911:key/1cf7d7fe-6974-40e3-bb0d-22b8c75d4eb8";

    /**
     * Init AeadConfig in the Tink library.
     */
    public AesGcmWithAwsKmsSavedKey() throws GeneralSecurityException {
        AeadConfig.register();
        AwsKmsClient.register(Optional.of(AWS_MASTER_KEY_URI), Optional.empty());
    }

    /**
     * Stores the encrypted keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    public void generateAndStoreKey(File keyset) throws IOException, GeneralSecurityException {
        if (!keyset.exists()) {
            AwsKmsClient awsKmsClient = (AwsKmsClient) KmsClients.get(AWS_MASTER_KEY_URI);
            KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES128_GCM"));
            keysetHandle.write(JsonKeysetWriter.withFile(keyset), awsKmsClient.getAead(AWS_MASTER_KEY_URI));
        }
    }

    public KeysetHandle loadKey(File keyset) throws IOException, GeneralSecurityException {
        AwsKmsClient awsKmsClient = (AwsKmsClient) KmsClients.get(AWS_MASTER_KEY_URI);

        return KeysetHandle.read(JsonKeysetReader.withFile(keyset), awsKmsClient.getAead(AWS_MASTER_KEY_URI));
    }

    public byte[] encrypt(KeysetHandle keysetHandle, byte[] initialText, byte[] associatedData) throws GeneralSecurityException {
        Aead aead = keysetHandle.getPrimitive(Aead.class);

        return aead.encrypt(initialText, associatedData);
    }

    public byte[] decrypt(KeysetHandle keysetHandle, byte[] cipherText, byte[] associatedData) throws GeneralSecurityException {
        Aead aead = keysetHandle.getPrimitive(Aead.class);

        return aead.decrypt(cipherText, associatedData);
    }
}
