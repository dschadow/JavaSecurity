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

import org.owasp.esapi.errors.AccessControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Servlet downloading files from inside the web application.
 *
 * @author Dominik Schadow
 */
@WebServlet(name = "DownloadServlet", urlPatterns = { "/download" })
public class DownloadServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadServlet.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String indirectReference = request.getParameter("file");

		LOGGER.info("File {} requested for download", indirectReference);

		InputStream is = null;
		OutputStream os = null;

		try {
			File file = ReferenceUtil.getFileByIndirectReference(indirectReference);

			is = new FileInputStream(file);

			byte[] bytes = new byte[1024];  
			os = response.getOutputStream();

			int read;
			while((read = is.read(bytes)) != -1) {  
				os.write(bytes, 0, read);  
			}  
			os.flush();
		} catch (AccessControlException ex) {
			LOGGER.error(ex.getMessage(), ex);
		} finally {
			if (os != null) {
				os.close();
			}

			if (is != null) {
				is.close();
			}
		}
	}
}
