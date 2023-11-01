package com.audiobea.crm.app.controller.customer;

import com.audiobea.crm.app.business.ICustomerService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/admin/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<ResponseData<DtoInCustomer>> getCustomers(
            @RequestParam(name = "firstName", defaultValue = "", required = false) String firstName,
            @RequestParam(name = "firstLastName", defaultValue = "", required = false) String firstLastName,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

        return new ResponseEntity<>(customerService.getCustomers(firstName, firstLastName, page, pageSize), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInCustomer> addCustomer(@RequestBody DtoInCustomer customer) {
        return new ResponseEntity<>(customerService.saveCustomer(customer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInCustomer> updateCustomer(@PathVariable(name = "id") Long id, @RequestBody DtoInCustomer customer) {
        return new ResponseEntity<>(customerService.updateCustomer(id, customer), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity<DtoInCustomer> getCustomerById(@PathVariable(name = "id") Long id, Principal principal,
                                                         Authentication authentication) {
        return new ResponseEntity<>(customerService.getCustomerById(id, authentication), HttpStatus.CREATED);
    }
}
