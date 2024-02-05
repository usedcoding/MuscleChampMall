package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDTO {

    //새로운 닉네임
    @NotBlank(message = "닉네임을 입력해 주세요")
    private String newNickname;

    //새로운 이메일
    @Email
    @NotBlank(message = "새로운 이메일을 입력해 주세요.")
    private String newEmail;

    //새로운 전화번호
    @NotBlank(message = "전화번호를 입력해 주새요")
    private String newPhoneNumber;

    //새로운 주소
    @NotBlank(message = "주소를 입력해 주세요")
    private String newAddress;
}
