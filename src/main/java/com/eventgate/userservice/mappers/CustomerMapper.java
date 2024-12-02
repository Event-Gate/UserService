package com.eventgate.userservice.mappers;

import com.eventgate.userservice.dtos.DataCustomerResponse;
import com.eventgate.userservice.entities.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public DataCustomerResponse toDataCustomerResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return new DataCustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail()
        );
    }
}
