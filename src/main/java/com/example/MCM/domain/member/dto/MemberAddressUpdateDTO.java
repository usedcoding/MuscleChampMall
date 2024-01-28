package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberAddressUpdateDTO {

    //새로운 주소
    @NotBlank(message = "주소를 입력해 주세요")
    private String newAddress;
}