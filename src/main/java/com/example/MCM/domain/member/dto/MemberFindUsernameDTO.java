package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberFindUsernameDTO {
    //아이디 찾기 DTO

    @NotEmpty(message = "이메일을 확인해 주세요.")
    private String email;

    @NotEmpty(message = "전화번호를 확인해 주세요.")
    private String phoneNumber;
}
