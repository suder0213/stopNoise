package com.ll.stopnoise.domain.customer.controller.dto;

import com.ll.stopnoise.domain.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CustomerReadDto {
    private int id;
    private String name;
    private String dong;
    private String ho;
    private List<Integer> postIds;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static CustomerReadDto from(Customer customer) {
        List<Integer> postIds = new ArrayList<>();
        if (customer.getPosts() != null) {
            customer.getPosts().forEach(post -> postIds.add(post.getId()));
        }
        CustomerReadDto dto = CustomerReadDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .dong(customer.getDong())
                .ho(customer.getHo())
                .postIds(postIds)
                .createAt(customer.getCreatedAt())
                .updateAt(customer.getUpdatedAt())
                .build();
        return dto;
    }
}
