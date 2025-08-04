package com.ll.stopnoise.domain.customer.service;

import com.ll.stopnoise.domain.customer.Repository.CustomerRepository;
import com.ll.stopnoise.domain.customer.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(String name) {
        return customerRepository.save( Customer.builder().name(name).build() );
    }

    public Customer getCustomer(int id) {
        Optional<Customer> customer = customerRepository.findById( id );

        if (customer.isEmpty()) {
            throw new IllegalArgumentException( "Customer not found" );
        }
        return customer.get();
    }

    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Transactional
    public Customer updateCustomer(Customer customer) {
        Optional<Customer> customer_ = customerRepository.findById(customer.getId());
        if (customer_.isEmpty()) {
            throw new IllegalArgumentException( "Customer not found" );
        }
        Customer customerToUpdate = customer_.get();
        customerToUpdate.setName( customer.getName() );
        return customerRepository.save( customerToUpdate );
    }

    public void deleteCustomer(int id) {
        Optional<Customer> customer = customerRepository.findById( id );
        if (customer.isEmpty()) {
            throw new IllegalArgumentException( "Customer not found" );
        }
        customerRepository.delete( customer.get() );
    }
}
