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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DownloadController.class)
class DownloadControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DownloadService downloadService;

    @Test
    void index_returnsIndexViewWithIndirectReferences() throws Exception {
        Set<String> indirectReferences = Set.of("ref1", "ref2");
        when(downloadService.getAllIndirectReferences()).thenReturn(indirectReferences);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("indirectReferences"))
                .andExpect(model().attribute("indirectReferences", containsInAnyOrder("ref1", "ref2")));
    }

    @Test
    void download_withValidReference_returnsResource() throws Exception {
        String indirectReference = "validRef";
        String filename = "test.pdf";
        File mockFile = new File(filename);
        Resource mockResource = new ByteArrayResource("test content".getBytes());

        when(downloadService.getFileByIndirectReference(indirectReference)).thenReturn(mockFile);
        when(downloadService.loadAsResource(filename)).thenReturn(mockResource);

        mockMvc.perform(get("/download").param("name", indirectReference))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"));
    }

    @Test
    void download_withMalformedUrl_returnsNotFound() throws Exception {
        String indirectReference = "malformedRef";
        String filename = "test.pdf";
        File mockFile = new File(filename);

        when(downloadService.getFileByIndirectReference(indirectReference)).thenReturn(mockFile);
        when(downloadService.loadAsResource(filename)).thenThrow(new MalformedURLException("Invalid URL"));

        mockMvc.perform(get("/download").param("name", indirectReference))
                .andExpect(status().isNotFound());
    }

    @Test
    void download_withJpgFile_returnsCorrectContentType() throws Exception {
        String indirectReference = "jpgRef";
        String filename = "image.jpg";
        File mockFile = new File(filename);
        Resource mockResource = new ByteArrayResource("image content".getBytes());

        when(downloadService.getFileByIndirectReference(indirectReference)).thenReturn(mockFile);
        when(downloadService.loadAsResource(filename)).thenReturn(mockResource);

        mockMvc.perform(get("/download").param("name", indirectReference))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));
    }
}
