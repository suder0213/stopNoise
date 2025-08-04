package com.ll.stopnoise.domain.customer.controller;

import com.ll.stopnoise.domain.customer.controller.dto.CustomerCreateDto;
import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public Customer createCustomer(@RequestBody CustomerCreateDto customerCreateDto) {
        return customerService.createCustomer(customerCreateDto.getName());
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable int id) {
        return customerService.getCustomer(id);
    }
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomer();
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomer(id);
    }

}
