/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.owasp.esapi.errors.AccessControlException;
import org.owasp.esapi.reference.RandomAccessReferenceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class takes care of the mapping between an indirect object reference (random String) and the concrete object (file).
 *
 * @author Dominik Schadow
 */
public class ReferenceUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceUtil.class);
    private static final Set<Object> files = new HashSet<>();
    private static final RandomAccessReferenceMap REFERENCE_MAP = new RandomAccessReferenceMap(files);

    /**
     * Private constructor to avoid initialization.
     */
    private ReferenceUtil() {
    }

    static {
        File coverImage = new File("src/main/webapp/resources/files/cover.jpg");
        REFERENCE_MAP.addDirectReference(coverImage);
        files.add(coverImage);

        File coverPdf = new File("src/main/webapp/resources/files/cover.pdf");
        REFERENCE_MAP.addDirectReference(coverPdf);
        files.add(coverPdf);
    }

    public static Set<String> getAllIndirectReferences() {
        Set<String> indirectReferences = new HashSet<>();

        for (Object file : files) {
            String indirectReference = REFERENCE_MAP.getIndirectReference(file);
            indirectReferences.add(indirectReference);
        }

        return indirectReferences;
    }

    public static String getFileNameByIndirectReference(String indirectReference) throws AccessControlException {
        File file = REFERENCE_MAP.getDirectReference(indirectReference);

        LOGGER.info("File name {}, path {}", file.getName(), file.getAbsolutePath());

        return file.getName();
    }

    public static File getFileByIndirectReference(String indirectReference) throws AccessControlException {
        File file = REFERENCE_MAP.getDirectReference(indirectReference);

        LOGGER.info("File name {}, path {}", file.getName(), file.getAbsolutePath());

        return file;
    }
}
