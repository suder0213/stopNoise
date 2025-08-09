package com.ll.stopnoise.domain.customer.service;

import com.ll.stopnoise.domain.customer.Repository.CustomerRepository;
import com.ll.stopnoise.domain.customer.controller.dto.CustomerCreateDto;
import com.ll.stopnoise.domain.customer.controller.dto.CustomerUpdateDto;
import com.ll.stopnoise.domain.customer.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(CustomerCreateDto customerCreateDto) {
        return customerRepository.save( Customer.builder()
                        .name(customerCreateDto.getName())
                        .dong(customerCreateDto.getDong())
                        .ho(customerCreateDto.getHo())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                .build() );
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

    
    // id로 찾아서 name만 변경 가능
    @Transactional
    public Customer updateCustomer(CustomerUpdateDto customerUpdateDto) {
        Optional<Customer> customer_ = customerRepository.findById(customerUpdateDto.getId());
        if (customer_.isEmpty()) {
            throw new IllegalArgumentException( "Customer not found" );
        }
        Customer customerToUpdate = customer_.get();
        customerToUpdate.setName( customerUpdateDto.getName() );
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
