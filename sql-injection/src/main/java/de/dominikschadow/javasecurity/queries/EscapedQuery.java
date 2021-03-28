/*
 * Copyright (C) 2021 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.javasecurity.queries;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.OracleCodec;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Servlet using a normal Statement to query the in-memory-database. User input is escaped with ESAPI and used in the
 * SQL query afterwards.
 *
 * @author Dominik Schadow
 */
@Component
public class EscapedQuery {
    private final JdbcTemplate jdbcTemplate;

    public EscapedQuery(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Customer> query(String name) {
        String safeName = ESAPI.encoder().encodeForSQL(new OracleCodec(), name);

        String query = "SELECT * FROM customer WHERE name = '" + safeName + "' ORDER BY id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        return CustomerRowMapper.mapRows(rows);
    }
}
