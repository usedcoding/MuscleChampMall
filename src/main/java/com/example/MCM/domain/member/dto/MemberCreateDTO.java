package com.example.MCM.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateDTO {

    @NotEmpty(message = "아이디는 필수 사항 입니다.")
    private String username;

    @NotEmpty(message = "닉네임은 필수 사항 입니다.")
    private String nickname;

    @NotEmpty(message = "이메일은 필수 사항 입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 사항 입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호를 확앤해 주세요.")
    private String password2;

    @NotEmpty(message = "전화번호는 필수 사항 입니다.")
    private String phoneNumber;

    @NotEmpty(message = "필수 사항 입니다.")
    private String mailKey;

}
