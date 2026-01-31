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
package de.dominikschadow.javasecurity.customers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void home_shouldReturnIndexViewWithModelAttributes() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("simple"))
                .andExpect(model().attributeExists("escaped"))
                .andExpect(model().attributeExists("prepared"));
    }

    @Test
    void simpleQuery_shouldReturnResultViewWithCustomers() throws Exception {
        Customer customer = createTestCustomer();
        when(customerService.simpleQuery(anyString())).thenReturn(List.of(customer));

        mockMvc.perform(post("/simple")
                        .param("name", "TestCustomer"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    void simpleQuery_withNoResults_shouldReturnEmptyList() throws Exception {
        when(customerService.simpleQuery(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/simple")
                        .param("name", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    void escapedQuery_shouldReturnResultViewWithCustomers() throws Exception {
        Customer customer = createTestCustomer();
        when(customerService.escapedQuery(anyString())).thenReturn(List.of(customer));

        mockMvc.perform(post("/escaped")
                        .param("name", "TestCustomer"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    void escapedQuery_withNoResults_shouldReturnEmptyList() throws Exception {
        when(customerService.escapedQuery(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/escaped")
                        .param("name", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    void preparedStatementQuery_shouldReturnResultViewWithCustomers() throws Exception {
        Customer customer = createTestCustomer();
        when(customerService.preparedStatementQuery(anyString())).thenReturn(List.of(customer));

        mockMvc.perform(post("/prepared")
                        .param("name", "TestCustomer"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    void preparedStatementQuery_withNoResults_shouldReturnEmptyList() throws Exception {
        when(customerService.preparedStatementQuery(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/prepared")
                        .param("name", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("customers"));
    }

    private Customer createTestCustomer() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("TestCustomer");
        customer.setStatus("Gold");
        customer.setOrderLimit(1000);
        return customer;
    }
}
