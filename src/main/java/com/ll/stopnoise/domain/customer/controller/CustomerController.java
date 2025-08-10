package com.ll.stopnoise.domain.customer.controller;

import com.ll.stopnoise.domain.customer.controller.dto.CustomerCreateDto;
import com.ll.stopnoise.domain.customer.controller.dto.CustomerReadDto;
import com.ll.stopnoise.domain.customer.controller.dto.CustomerUpdateDto;
import com.ll.stopnoise.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public CustomerReadDto createCustomer(@RequestBody CustomerCreateDto customerCreateDto) {
        return CustomerReadDto.from(customerService.createCustomer(customerCreateDto));
    }

    @GetMapping
    public List<CustomerReadDto> getAllCustomers() {
        return customerService.getAllCustomer().stream().map(CustomerReadDto::from).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CustomerReadDto getCustomer(@PathVariable int id) {
        return CustomerReadDto.from(customerService.getCustomer(id));
    }


    @PutMapping
    public CustomerReadDto updateCustomer(@RequestBody CustomerUpdateDto customerUpdateDto) {
        return CustomerReadDto.from(customerService.updateCustomer(customerUpdateDto));
    }

    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomer(id);
        return "Customer %d deleted".formatted(id);
    }

}
