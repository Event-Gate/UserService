package com.eventgate.userservice.controllers.authenticated;

import com.eventgate.userservice.dtos.DataCustomerRequest;
import com.eventgate.userservice.dtos.DataCustomerResponse;
import com.eventgate.userservice.entities.Customer;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import com.eventgate.userservice.mappers.CustomerMapper;
import com.eventgate.userservice.services.interfaces.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequiredArgsConstructor @PreAuthorize("isAuthenticated()") @RequestMapping("/api/auth/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PutMapping("/update")
    public ResponseEntity<DataCustomerResponse> updateCurrentCustomer(@Valid @RequestBody DataCustomerRequest request) throws UnauthorizedException {
        Customer updatedCustomer = customerService.updateAuthenticatedCustomerDetails(request);
        DataCustomerResponse response = customerMapper.toDataCustomerResponse(updatedCustomer);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
