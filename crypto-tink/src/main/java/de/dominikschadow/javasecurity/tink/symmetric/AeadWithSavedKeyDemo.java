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
import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import de.dominikschadow.javasecurity.tink.TinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the Authenticated Encryption with Associated Data (AEAD) primitive. The used
 * key is stored and loaded from the project.
 *
 * @author Dominik Schadow
 */
public class AeadWithSavedKeyDemo {
    private static final Logger log = LoggerFactory.getLogger(AeadWithSavedKeyDemo.class);
    private static final String INITIAL_TEXT = "Some dummy text to work with";
    private static final String ASSOCIATED_DATA = "Some additional data";
    private static final String KEYSET_FILENAME = "crypto-tink/src/main/resources/keysets/aead-keyset.json";

    /**
     * Init AEAD in the Tink library.
     */
    private AeadWithSavedKeyDemo() {
        try {
            AeadConfig.register();
        } catch (GeneralSecurityException ex) {
            log.error("Failed to initialize Tink", ex);
        }
    }

    public static void main(String[] args) {
        AeadWithSavedKeyDemo demo = new AeadWithSavedKeyDemo();

        try {
            demo.generateAndStoreKey();

            KeysetHandle keysetHandle = demo.loadKey();
            
            byte[] cipherText = demo.encrypt(keysetHandle);
            byte[] plainText = demo.decrypt(keysetHandle, cipherText);

            demo.printCryptoData(keysetHandle, cipherText, plainText);
        } catch (GeneralSecurityException ex) {
            log.error("Failure during Tink usage", ex);
        } catch (IOException ex) {
            log.error("Failure during storing key", ex);
        }
    }

    /**
     * Stores the keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    private void generateAndStoreKey() throws IOException, GeneralSecurityException {
        KeysetHandle keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);

        File keysetFile = new File(KEYSET_FILENAME);

        if (!keysetFile.exists()) {
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keysetFile));
        }
    }

    private KeysetHandle loadKey() throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File(KEYSET_FILENAME)));
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
        log.info("keyset data: {}", TinkUtils.printKeyset(keysetHandle));
    }
}
