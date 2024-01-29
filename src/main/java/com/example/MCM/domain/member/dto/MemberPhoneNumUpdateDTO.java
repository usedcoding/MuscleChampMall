package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberPhoneNumUpdateDTO {

    //새로운 전화번호
    @NotBlank(message = "전화번호를 입력해 주새요")
    private String newPhoneNumber;
}
