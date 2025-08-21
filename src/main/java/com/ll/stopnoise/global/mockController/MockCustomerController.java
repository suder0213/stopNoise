package com.ll.stopnoise.global.mockController;

import com.ll.stopnoise.domain.customer.controller.dto.CustomerCreateDto;
import com.ll.stopnoise.domain.customer.controller.dto.CustomerReadDto;
import com.ll.stopnoise.domain.customer.controller.dto.CustomerUpdateDto;
import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.global.RsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mock/api/customer")
public class MockCustomerController {
    private Customer customer1 = Customer.builder()
            .id(1)
            .dong("101")
            .ho("101")
            .name("홍길동")
            .createdAt(LocalDateTime.now())
            .build();
    private Customer customer2 = Customer.builder()
            .id(2)
            .dong("102")
            .ho("102")
            .name("김춘배")
            .createdAt(LocalDateTime.now())
            .build();

    @PostMapping
    public ResponseEntity<RsData<CustomerReadDto>> createCustomer(@RequestBody CustomerCreateDto customerCreateDto) {
        Customer customer = Customer.builder()
                .id(1)
                .dong(customerCreateDto.getDong())
                .ho(customerCreateDto.getHo())
                .name(customerCreateDto.getName())
                .createdAt(LocalDateTime.now())
                .build();
        CustomerReadDto dto = CustomerReadDto.from(customer);
        RsData<CustomerReadDto> response = RsData.of("S-1", "성공적으로 생성됨", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET: 모든 고객 조회
    @GetMapping
    public ResponseEntity<RsData<List<CustomerReadDto>>> getAllCustomers() {
        List<Customer> tmp = new ArrayList<>();
        tmp.add(customer1);
        tmp.add(customer2);
        List<CustomerReadDto> dtoList = tmp.stream().map(CustomerReadDto::from).collect(Collectors.toList());
        RsData<List<CustomerReadDto>> response = RsData.of("S-1", "성공적으로 조회됨", dtoList);
        return ResponseEntity.ok(response);
    }

    // GET: 특정 고객 조회
    @GetMapping("/{id}")
    public ResponseEntity<RsData<CustomerReadDto>> getCustomer(@PathVariable int id) {
        Customer customer;
        if (id == 1 || id == 2) {
            if (id == 1) customer = customer1;
            else customer = customer2;
            CustomerReadDto dto = CustomerReadDto.from(customer);
            RsData<CustomerReadDto> response = RsData.of("S-1", "성공적으로 조회됨", dto);
            return ResponseEntity.ok(response);
        } else {
            RsData<CustomerReadDto> response = RsData.of("F-1", "해당 고객을 찾을 수 없음", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    // PUT: 고객 정보 수정
    @PutMapping
    public ResponseEntity<RsData<CustomerReadDto>> updateCustomer(@RequestBody CustomerUpdateDto customerUpdateDto) {
        Customer customer = customer1;
        CustomerReadDto dto = CustomerReadDto.from(customer);
        RsData<CustomerReadDto> response = RsData.of("S-1", "성공적으로 수정됨", dto);
        return ResponseEntity.ok(response);
    }

    // DELETE: 고객 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<RsData<String>> deleteCustomer(@PathVariable int id) {
        if (id == 1 || id == 2) {
            RsData<String> response = RsData.of("S-1", "성공적으로 삭제됨", null);
            return ResponseEntity.ok(response);
        } else {
            RsData<String> response = RsData.of("F-1", "해당 고객을 찾을 수 없음", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
