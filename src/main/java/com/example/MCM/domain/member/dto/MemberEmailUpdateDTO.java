package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberEmailUpdateDTO {

    //새로운 이메일
    @Email
    @NotBlank(message = "새로운 이메일을 입력해 주세요.")
    private String newEmail;
}
