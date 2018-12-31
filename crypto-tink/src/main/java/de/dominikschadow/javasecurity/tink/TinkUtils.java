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
package de.dominikschadow.javasecurity.tink;

import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeysetHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Google Tink utils for demo projects.
 *
 * @author Dominik Schadow
 */
public class TinkUtils {
    private static final Logger log = LoggerFactory.getLogger(TinkUtils.class);

    public static String printKeyset(KeysetHandle keysetHandle) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withOutputStream(outputStream));
            return new String(outputStream.toByteArray());
        } catch (IOException ex) {
            log.error("Failed to write keyset", ex);
        }

        return "";
    }
}
