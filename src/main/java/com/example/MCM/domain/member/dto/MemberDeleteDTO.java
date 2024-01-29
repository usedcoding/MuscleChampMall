package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDeleteDTO {

    @NotBlank(message = "비밀번호를 확인해 주세요.")
    private String confirmPassword;
}
