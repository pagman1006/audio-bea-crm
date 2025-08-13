package com.audiobea.crm.app.controller.customer;

import com.audiobea.crm.app.business.ICustomerService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerTest {

    private MockMvc mockMvc;
    private ICustomerService customerService;

    @BeforeEach
    void setup() {
        customerService = Mockito.mock(ICustomerService.class);
        CustomerController controller = new CustomerController(customerService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /admin/customers returns 200 with pagination fields")
    void getCustomers_ReturnsOk() throws Exception {
        ResponseData<DtoInCustomer> response = new ResponseData<>();
        response.setPage(1);
        response.setPageSize(10);
        response.setTotalElements(0L);
        response.setTotalPages(0);
        // data field is transient; not asserted
        given(customerService.getCustomers(anyString(), anyString(), anyInt(), anyInt())).willReturn(response);

        mockMvc.perform(get("/admin/customers")
                        .param("name", "John")
                        .param("lastName", "Doe")
                        .param("page", "1")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));

        verify(customerService).getCustomers(eq("John"), eq("Doe"), eq(1), eq(10));
    }

    @Test
    @DisplayName("POST /admin/customers returns 201 and echoes created customer")
    void addCustomer_ReturnsCreated() throws Exception {
        DtoInCustomer input = new DtoInCustomer();
        input.setName("Alice");
        input.setLastName("Smith");

        DtoInCustomer saved = new DtoInCustomer();
        saved.setId("123");
        saved.setName("Alice");
        saved.setLastName("Smith");

        given(customerService.saveCustomer(any(DtoInCustomer.class))).willReturn(saved);

        String body = "{\n" +
                "  \"name\": \"Alice\",\n" +
                "  \"lastName\": \"Smith\"\n" +
                "}";

        mockMvc.perform(post("/admin/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Smith"));

        ArgumentCaptor<DtoInCustomer> captor = ArgumentCaptor.forClass(DtoInCustomer.class);
        verify(customerService).saveCustomer(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Alice");
        assertThat(captor.getValue().getLastName()).isEqualTo("Smith");
    }

    @Test
    @DisplayName("PUT /admin/customers/{id} returns 201 with updated customer")
    void updateCustomer_ReturnsCreated() throws Exception {
        DtoInCustomer updated = new DtoInCustomer();
        updated.setId("abc");
        updated.setName("Bob");
        updated.setLastName("Marley");

        given(customerService.updateCustomer(eq("abc"), any(DtoInCustomer.class))).willReturn(updated);

        String body = "{\n" +
                "  \"name\": \"Bob\",\n" +
                "  \"lastName\": \"Marley\"\n" +
                "}";

        mockMvc.perform(put("/admin/customers/{id}", "abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("abc"))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.lastName").value("Marley"));

        verify(customerService).updateCustomer(eq("abc"), any(DtoInCustomer.class));
    }

    @Test
    @DisplayName("GET /admin/customers/{id} returns 201 with the customer")
    void getCustomerById_ReturnsCreated() throws Exception {
        DtoInCustomer customer = new DtoInCustomer();
        customer.setId("c1");
        customer.setName("Carol");
        customer.setLastName("Danvers");

        // Authentication is null in standalone setup, so expect null
        given(customerService.getCustomerById(eq("c1"), isNull())).willReturn(customer);

        mockMvc.perform(get("/admin/customers/{id}", "c1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("c1"))
                .andExpect(jsonPath("$.name").value("Carol"))
                .andExpect(jsonPath("$.lastName").value("Danvers"));

        verify(customerService).getCustomerById(eq("c1"), isNull());
    }
}
