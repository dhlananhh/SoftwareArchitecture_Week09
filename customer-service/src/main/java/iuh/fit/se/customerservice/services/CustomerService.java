package iuh.fit.se.customerservice.services;

import iuh.fit.se.customerservice.entities.Customer;

import java.util.List;

public interface CustomerService {
    public Customer save(Customer customer);
    public Customer findById(long id);
    List<Customer> getAllCustomers();
    void deleteCustomer(Long id);
}
