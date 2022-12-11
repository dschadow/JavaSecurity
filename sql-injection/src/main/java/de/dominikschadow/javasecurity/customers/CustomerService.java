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
package de.dominikschadow.javasecurity.customers;

import lombok.RequiredArgsConstructor;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.OracleCodec;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service to query the in-memory-database.
 *
 * <ul>
 *     <li>Using a prepared statement: User input is not modified and used directly in the SQL query.</li>
 *     <li>Using an escaped statement: User input is escaped with ESAPI and used in the SQL query afterwards.</li>
 *     <li>Using a plain statement: User input is not modified and used directly in the SQL query.</li>
 * </ul>
 *
 * {@code ' or '1'='1} is a good input to return all data, {@code '; drop table customer;--} to delete the complete table.
 *
 * @author Dominik Schadow
 */
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final JdbcTemplate jdbcTemplate;

    List<Customer> preparedStatementQuery(String name) {
        String query = "SELECT * FROM customers WHERE name = ? ORDER BY id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, name);

        return mapRows(rows);
    }

    List<Customer> escapedQuery(String name) {
        String safeName = ESAPI.encoder().encodeForSQL(new OracleCodec(), name);

        String query = "SELECT * FROM customers WHERE name = '" + safeName + "' ORDER BY id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        return mapRows(rows);
    }

    List<Customer> simpleQuery(String name) {
        String query = "SELECT * FROM customers WHERE name = '" + name + "' ORDER BY id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        return mapRows(rows);
    }

    private List<Customer> mapRows(List<Map<String, Object>> rows) {
        List<Customer> customers = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Customer customer = new Customer();
            customer.setId((Integer) row.get("id"));
            customer.setName((String) row.get("name"));
            customer.setStatus((String) row.get("status"));
            customer.setOrderLimit((Integer) row.get("order_limit"));

            customers.add(customer);
        }

        return customers;
    }
}
