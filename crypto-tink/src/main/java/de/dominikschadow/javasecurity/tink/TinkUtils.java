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
package de.dominikschadow.javasecurity.tink;

import com.google.common.io.BaseEncoding;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeysetHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Google Tink utils for this demo project.
 *
 * @author Dominik Schadow
 */
public class TinkUtils {
    private static final Logger log = LoggerFactory.getLogger(TinkUtils.class);
    public static final String AWS_MASTER_KEY_URI = "aws-kms://arn:aws:kms:eu-central-1:776241929911:key/cce9ce6d-526c-44ca-9189-45c54b90f070";

    public static void printKeyset(String type, KeysetHandle keysetHandle) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withOutputStream(outputStream));

            log.info("{}: {}", type, outputStream.toString());
        } catch (IOException ex) {
            log.error("Failed to write keyset", ex);
        }
    }

    public static void printSymmetricEncryptionData(KeysetHandle keysetHandle, String initialText, byte[] cipherText, byte[] plainText) {
        log.info("initial text: {}", initialText);
        log.info("cipher text: {}", BaseEncoding.base16().encode(cipherText));
        log.info("plain text: {}", new String(plainText, StandardCharsets.UTF_8));

        printKeyset("keyset data", keysetHandle);
    }

    public static void printHybridEncryptionData(KeysetHandle privateKeysetHandle, KeysetHandle publicKeysetHandle, String initialText, byte[] cipherText, byte[] plainText) {
        log.info("initial text: {}", initialText);
        log.info("cipher text: {}", BaseEncoding.base16().encode(cipherText));
        log.info("plain text: {}", new String(plainText, StandardCharsets.UTF_8));

        printKeyset("private key set data", privateKeysetHandle);
        printKeyset("public key set data", publicKeysetHandle);
    }

    public static void printMacData(KeysetHandle keysetHandle, String initialText, byte[] tag, boolean valid) {
        log.info("initial text: {}", initialText);
        log.info("MAC: {}", BaseEncoding.base16().encode(tag));
        log.info("MAC is valid: {}", valid);

        printKeyset("keyset data", keysetHandle);
    }

    public static void printSignatureData(KeysetHandle privateKeysetHandle, KeysetHandle publicKeysetHandle, String initialText, byte[] signature, boolean valid) {
        log.info("initial text: {}", initialText);
        log.info("signature: {}", BaseEncoding.base16().encode(signature));
        log.info("signature is valid: {}", valid);

        printKeyset("private key set data", privateKeysetHandle);
        printKeyset("public key set data", publicKeysetHandle);
    }
}
