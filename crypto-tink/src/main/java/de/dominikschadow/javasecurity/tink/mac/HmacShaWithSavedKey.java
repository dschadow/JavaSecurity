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
package de.dominikschadow.javasecurity.tink.mac;

import com.google.crypto.tink.*;
import com.google.crypto.tink.mac.MacConfig;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Shows crypto usage with Google Tink for the Hash-based Message Authentication Code (HMAC) primitive. The used key is
 * stored and loaded from the project. Selected algorithm is SHA 256 with 128 bit.
 *
 * @author Dominik Schadow
 */
public class HmacShaWithSavedKey {
    /**
     * Init MacConfig in the Tink library.
     */
    public HmacShaWithSavedKey() throws GeneralSecurityException {
        MacConfig.register();
    }

    /**
     * Stores the keyset in the projects resources/keysets directory if it does not exist yet.
     *
     * @throws IOException              Failure during saving
     * @throws GeneralSecurityException Failure during keyset generation
     */
    public void generateAndStoreKey(File keyset) throws IOException, GeneralSecurityException {
        if (!keyset.exists()) {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(KeyTemplates.get("HMAC_SHA256_128BITTAG"));
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keyset));
        }
    }

    public KeysetHandle loadKey(File keyset) throws IOException, GeneralSecurityException {
        return CleartextKeysetHandle.read(JsonKeysetReader.withFile(keyset));
    }

    public byte[] computeMac(KeysetHandle keysetHandle, byte[] initialText) throws GeneralSecurityException {
        Mac mac = keysetHandle.getPrimitive(Mac.class);

        return mac.computeMac(initialText);
    }

    public boolean verifyMac(KeysetHandle keysetHandle, byte[] initialMac, byte[] initialText) {
        try {
            Mac mac = keysetHandle.getPrimitive(Mac.class);
            mac.verifyMac(initialMac, initialText);

            return true;
        } catch (GeneralSecurityException ex) {
            // MAC is invalid
            return false;
        }
    }
}