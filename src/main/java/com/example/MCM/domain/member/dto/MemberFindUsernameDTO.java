package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFindUsernameDTO {
    //아이디 찾기 DTO

    @NotBlank(message = "이메일을 확인해 주세요.")
    private String email;

    @NotBlank(message = "전화번호를 확인해 주세요.")
    private String phoneNumber;
}
