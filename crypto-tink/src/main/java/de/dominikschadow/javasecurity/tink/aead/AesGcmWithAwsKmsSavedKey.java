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
package de.dominikschadow.javasecurity.tink.aead;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the Authenticated Encryption with Associated Data (AEAD) primitive. The used
 * key is stored and loaded from AWS KMS. Requires a master key available in AWS KMS and correctly configured
 * credentials to access AWS KMS: AWS_ACCESS_KEY_ID and AWS_SECRET_KEY must be set as environment variables.
 * <p>
 * Selected algorithm is AES-GCM with 128 bit.
 *
 * @author Dominik Schadow
 * @see <a href="https://docs.aws.amazon.com/kms/latest/developerguide/create-keys.html">Creating Keys</a>
 * @see <a href="https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html#credentials-default">Using
 * the Default Credential Provider Chain</a>
 */
public class AesGcmWithAwsKmsSavedKey {
    private static final Logger log = LoggerFactory.getLogger(AesGcmWithAwsKmsSavedKey.class);

    /**
     * Init AeadConfig in the Tink library.
     */
    public AesGcmWithAwsKmsSavedKey() {
        try {
            AeadConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    /**
     * Stores the encrypted keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    public void generateAndStoreKey(File keyset, String awsMasterKeyUri) throws IOException, GeneralSecurityException {
        if (!keyset.exists()) {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES128_GCM"));
            keysetHandle.write(JsonKeysetWriter.withFile(keyset), new AwsKmsClient().withDefaultCredentials().getAead(awsMasterKeyUri));
        }
    }

    public KeysetHandle loadKey(File keyset, String awsMasterKeyUri) throws IOException, GeneralSecurityException {
        return KeysetHandle.read(JsonKeysetReader.withFile(keyset),
                new AwsKmsClient().withDefaultCredentials().getAead(awsMasterKeyUri));
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
