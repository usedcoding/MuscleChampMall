package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberPasswordUpdateDTO {

    @NotBlank(message = "기존 비밀번호를 입력해 주세요")
    private String currentPassword;

    @NotBlank(message = "새로운 비밀번호가 일치하지 않습니다.")
    private String confirmPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해 주세요")
    private String newPassword;
}
