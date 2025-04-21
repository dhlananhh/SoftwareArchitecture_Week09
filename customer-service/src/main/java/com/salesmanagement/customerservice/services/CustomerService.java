package com.salesmanagement.customerservice.services;

import java.util.List;
import java.util.Optional;
import com.salesmanagement.customerservice.entities.Customer;

public interface CustomerService {
	Customer createCustomer(Customer customer);
    Optional<Customer> getCustomerById(Long id);
    List<Customer> getAllCustomers();
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
}
