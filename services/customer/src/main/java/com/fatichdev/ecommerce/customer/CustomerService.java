package com.fatichdev.ecommerce.customer;

import com.fatichdev.ecommerce.exception.CustomerNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request) {
        var customer = repository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Cannot update customer:: No customer found with provided ID:: %s", request.id())
                ));
        mergerCustomer(customer, request);
        repository.save(customer);
    }

    private void mergerCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }

        if (StringUtils.isNotBlank(request.lastname())) {
            customer.setLastname(request.lastname());
        }

        if (StringUtils.isNotBlank(request.email())) {
            customer.setFirstname(request.email());
        }

        if (request.address() != null) {
            customer.setAddress(request.address());
        }


    }

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll().stream()
                .map(mapper::fromCustomer)
                .toList();
    }

    public Boolean existsById(String customerId) {
        return repository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return repository.findById(customerId)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("No customer found with provided ID:: %s", customerId)
                ));
    }

    public void deleteCustomer(String customerId) {
        if (!existsById(customerId)) {
            throw new CustomerNotFoundException(
                    String.format("Cannot delete customer:: No customer found with provided ID:: %s", customerId)
            );
        }
        repository.deleteById(customerId);
    }

}
