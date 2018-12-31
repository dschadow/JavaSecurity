/*
 * Copyright (C) 2018 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.tink.symmetric;

import com.google.common.io.BaseEncoding;
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the Authenticated Encryption with Associated Data (AEAD) primitive.
 *
 * @author Dominik Schadow
 */
public class AeadDemo {
    private static final Logger log = LoggerFactory.getLogger(AeadDemo.class);
    private static final String INITIAL_TEXT = "Some dummy text to work with";
    private static final String ASSOCIATED_DATA = "Some additional data";

    /**
     * Init AEAD in the Tink library.
     */
    private AeadDemo() {
        try {
            AeadConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize AeadConfig", ex);
        }
    }

    public static void main(String[] args) {

        AeadDemo demo = new AeadDemo();

        try {
            KeysetHandle keysetHandle = demo.generateKey();

            byte[] cipherText = demo.encrypt(keysetHandle);
            byte[] plainText = demo.decrypt(keysetHandle, cipherText);

            demo.printCryptoData(keysetHandle, cipherText, plainText);
        } catch (GeneralSecurityException ex) {
            log.error("Failed to encrypt/decrypt with AEAD", ex);
        }
    }

    private KeysetHandle generateKey() throws GeneralSecurityException {
        return KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);
    }

    private byte[] encrypt(KeysetHandle keysetHandle) throws GeneralSecurityException {
        Aead aead = AeadFactory.getPrimitive(keysetHandle);

        return aead.encrypt(INITIAL_TEXT.getBytes(), ASSOCIATED_DATA.getBytes());
    }

    private byte[] decrypt(KeysetHandle keysetHandle, byte[] cipherText) throws GeneralSecurityException {
        Aead aead = AeadFactory.getPrimitive(keysetHandle);

        return aead.decrypt(cipherText, ASSOCIATED_DATA.getBytes());
    }

    private void printCryptoData(KeysetHandle keysetHandle, byte[] cipherText, byte[] plainText) {
        log.info("initial text: {}", INITIAL_TEXT);
        log.info("cipher text: {}", BaseEncoding.base16().encode(cipherText));
        log.info("plain text: {}", new String(plainText, Charset.forName("UTF-8")));

        try {
            log.info("keyset data:");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withOutputStream(outputStream));
            log.info(new String(outputStream.toByteArray()));
        } catch (IOException ex) {
            log.error("Failed to write keyset", ex);
        }
    }
}
