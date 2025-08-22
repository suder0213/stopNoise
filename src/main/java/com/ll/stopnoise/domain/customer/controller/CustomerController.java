package com.ll.stopnoise.domain.customer.controller;

import com.ll.stopnoise.domain.customer.controller.dto.CustomerCreateDto;
import com.ll.stopnoise.domain.customer.controller.dto.CustomerReadDto;
import com.ll.stopnoise.domain.customer.controller.dto.CustomerUpdateDto;
import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.customer.service.CustomerService;
import com.ll.stopnoise.global.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.POST})
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<RsData<CustomerReadDto>> createCustomer(@RequestBody CustomerCreateDto customerCreateDto) {
        Customer customer = customerService.createCustomer(customerCreateDto);
        CustomerReadDto dto = CustomerReadDto.from(customer);
        RsData<CustomerReadDto> response = RsData.of("S-1", "성공적으로 생성됨", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET: 모든 고객 조회
    @GetMapping
    public ResponseEntity<RsData<List<CustomerReadDto>>> getAllCustomers() {
        List<CustomerReadDto> dtoList = customerService.getAllCustomer().stream()
                .map(CustomerReadDto::from)
                .collect(Collectors.toList());
        RsData<List<CustomerReadDto>> response = RsData.of("S-1", "성공적으로 조회됨", dtoList);
        return ResponseEntity.ok(response);
    }

    // GET: 특정 고객 조회
    @GetMapping("/{id}")
    public ResponseEntity<RsData<CustomerReadDto>> getCustomer(@PathVariable int id) {
        try {
            Customer customer = customerService.getCustomer(id);
            CustomerReadDto dto = CustomerReadDto.from(customer);
            RsData<CustomerReadDto> response = RsData.of("S-1", "성공적으로 조회됨", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<CustomerReadDto> response = RsData.of("F-1", "해당 고객을 찾을 수 없음", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    // PUT: 고객 정보 수정
    @PutMapping
    public ResponseEntity<RsData<CustomerReadDto>> updateCustomer(@RequestBody CustomerUpdateDto customerUpdateDto) {
        Customer customer = customerService.updateCustomer(customerUpdateDto);
        CustomerReadDto dto = CustomerReadDto.from(customer);
        RsData<CustomerReadDto> response = RsData.of("S-1", "성공적으로 수정됨", dto);
        return ResponseEntity.ok(response);
    }

    // DELETE: 고객 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<RsData<String>> deleteCustomer(@PathVariable int id) {
        try {
            customerService.deleteCustomer(id);
            RsData<String> response = RsData.of("S-1", "성공적으로 삭제됨", null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<String> response = RsData.of("F-1", "해당 고객을 찾을 수 없음", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
