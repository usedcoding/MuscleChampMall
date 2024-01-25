package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberNicknameUpdateDTO {

    @NotBlank(message = "닉네임을 입력해 주세요")
    private String newNickname;
}
