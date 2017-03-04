/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
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

import org.owasp.esapi.errors.AccessControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Servlet downloading files from inside the web application.
 *
 * @author Dominik Schadow
 */
@WebServlet(name = "DownloadServlet", urlPatterns = {"/download"})
public class DownloadServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(DownloadServlet.class);
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String indirectReference = request.getParameter("file");

        log.info("File {} requested for download", indirectReference);

        try (InputStream is = new FileInputStream(ReferenceUtil.getFileByIndirectReference(indirectReference)); OutputStream os = response.getOutputStream()) {
            byte[] bytes = new byte[1024];

            int read;
            while ((read = is.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
        } catch (AccessControlException | IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
