package com.eventgate.userservice.services.interfaces;

import com.eventgate.userservice.dtos.DataCustomerRequest;
import com.eventgate.userservice.entities.Customer;
import com.eventgate.userservice.exceptions.UnauthorizedException;

public interface CustomerService {
    Customer updateAuthenticatedCustomerDetails(DataCustomerRequest request) throws UnauthorizedException;
}
