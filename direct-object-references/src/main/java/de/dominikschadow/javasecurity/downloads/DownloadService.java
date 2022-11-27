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
package de.dominikschadow.javasecurity.downloads;

import jakarta.annotation.PostConstruct;
import org.owasp.esapi.errors.AccessControlException;
import org.owasp.esapi.reference.RandomAccessReferenceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

@Service
public class DownloadService {
    private static final Logger log = LoggerFactory.getLogger(DownloadService.class);
    private final Set<Object> resources = new HashSet<>();
    private final RandomAccessReferenceMap referenceMap = new RandomAccessReferenceMap(resources);
    private final String rootLocation;

    public DownloadService() {
        this.rootLocation = "http://localhost:8080/files/";
    }

    @PostConstruct
    protected void init() {
        File coverImage = new File("cover.pdf");
        referenceMap.addDirectReference(coverImage);
        resources.add(coverImage);

        File coverPdf = new File("cover.jpg");
        referenceMap.addDirectReference(coverPdf);
        resources.add(coverPdf);
    }

    Set<String> getAllIndirectReferences() {
        Set<String> indirectReferences = new HashSet<>();

        for (Object file : resources) {
            String indirectReference = referenceMap.getIndirectReference(file);
            indirectReferences.add(indirectReference);
        }

        return indirectReferences;
    }

    File getFileByIndirectReference(String indirectReference) throws AccessControlException {
        File file = referenceMap.getDirectReference(indirectReference);

        log.info("File name {}", file.getName());

        return file;
    }

    Resource loadAsResource(String filename) throws MalformedURLException {
        Resource resource = new UrlResource(rootLocation + filename);
        if (resource.exists() || resource.isReadable()) {
            return resource;
        }

        return null;
    }
}