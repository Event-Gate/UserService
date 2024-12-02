package com.eventgate.userservice.services.implementations;

import com.eventgate.userservice.utils.SecurityUtils;
import com.eventgate.userservice.dtos.DataCustomerRequest;
import com.eventgate.userservice.entities.Customer;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import com.eventgate.userservice.repositories.CustomerRepository;
import com.eventgate.userservice.services.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @RequiredArgsConstructor @Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final SecurityUtils securityUtils;

    @Override
    public Customer updateAuthenticatedCustomerDetails(DataCustomerRequest request) throws UnauthorizedException {
        Customer customer = securityUtils.getAuthenticatedCustomer();

        Optional.ofNullable(request.fullName())
                .filter(fullName -> !fullName.trim().isEmpty())
                .ifPresent(customer::setFullName);

        Optional.ofNullable(request.phone())
                .filter(phone -> !phone.trim().isEmpty())
                .ifPresent(customer::setPhone);

        return customerRepository.save(customer);
    }
}