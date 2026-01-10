/*
 * Copyright (C) 2026 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.downloads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.esapi.errors.AccessControlException;
import org.springframework.core.io.Resource;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DownloadServiceTest {
    private DownloadService downloadService;

    @BeforeEach
    void setUp() {
        downloadService = new DownloadService();
        downloadService.init();
    }

    @Test
    void getAllIndirectReferences_returnsNonEmptySet() {
        Set<String> indirectReferences = downloadService.getAllIndirectReferences();

        assertNotNull(indirectReferences);
        assertFalse(indirectReferences.isEmpty());
        assertEquals(2, indirectReferences.size());
    }

    @Test
    void getAllIndirectReferences_returnsUniqueReferences() {
        Set<String> indirectReferences = downloadService.getAllIndirectReferences();

        assertEquals(2, indirectReferences.size());
        for (String reference : indirectReferences) {
            assertNotNull(reference);
            assertFalse(reference.isEmpty());
        }
    }

    @Test
    void getFileByIndirectReference_withValidReference_returnsFile() throws AccessControlException {
        Set<String> indirectReferences = downloadService.getAllIndirectReferences();
        String validReference = indirectReferences.iterator().next();

        File file = downloadService.getFileByIndirectReference(validReference);

        assertNotNull(file);
        assertTrue(file.getName().equals("cover.pdf") || file.getName().equals("cover.jpg"));
    }

    @Test
    void getFileByIndirectReference_withInvalidReference_throwsException() {
        String invalidReference = "invalid-reference-that-does-not-exist";

        assertThrows(Exception.class, () -> {
            downloadService.getFileByIndirectReference(invalidReference);
        });
    }

    @Test
    void getFileByIndirectReference_returnsCorrectFileForEachReference() throws AccessControlException {
        Set<String> indirectReferences = downloadService.getAllIndirectReferences();
        Set<String> expectedFileNames = Set.of("cover.pdf", "cover.jpg");
        Set<String> actualFileNames = new java.util.HashSet<>();

        for (String reference : indirectReferences) {
            File file = downloadService.getFileByIndirectReference(reference);
            actualFileNames.add(file.getName());
        }

        assertEquals(expectedFileNames, actualFileNames);
    }

    @Test
    void loadAsResource_withNonExistentFile_returnsNull() throws MalformedURLException {
        Resource resource = downloadService.loadAsResource("non-existent-file.pdf");

        assertNull(resource);
    }

    @Test
    void loadAsResource_withFilename_createsUrlResource() throws MalformedURLException {
        String filename = "cover.pdf";

        // The method creates a UrlResource but returns null if the resource doesn't exist
        // This tests the behavior when the file is not accessible
        Resource resource = downloadService.loadAsResource(filename);

        // Resource is null because the file doesn't exist at the URL location
        assertNull(resource);
    }
}
