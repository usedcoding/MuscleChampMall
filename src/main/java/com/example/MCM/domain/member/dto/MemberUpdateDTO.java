package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDTO {

    //새로운 비밀번호
    @NotBlank(message = "새로운 비밀번호를 입력해 주세요")
    private String newPassword;

    //새로운 비밀번호 확인
    @NotBlank(message = "새로운 비밀번호가 일치하지 않습니다.")
    private String newPassword2;

    //새로운 닉네임
    @NotBlank(message = "새로운 닉네임을 입력해 주세요")
    private String newNickname;

    //새로운 이메일
    @Email
    @NotBlank(message = "새로운 이메일을 입력해 주세요.")
    private String newEmail;

    //새로운 전화번호
    @NotBlank(message = "새로운 전화번호를 입력해 주세요")
    private String newPhoneNumber;

    //새로운 주소
    @NotBlank(message = "새로운 주소를 입력해 주세요")
    private String newAddress;




}
