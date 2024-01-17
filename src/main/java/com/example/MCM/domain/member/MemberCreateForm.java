package com.example.MCM.domain.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemberCreateForm {

    @NotEmpty(message = "필수 사항 입니다.")
    private String username;

    @NotEmpty(message = "필수 사항 입니다.")
    private String nickname;

    @NotEmpty(message = "필수 사항 입니다.")
    private String email;

    @NotEmpty(message = "필수 사항 입니다.")
    private String password1;

    @NotEmpty(message = "필수 사항 입니다.")
    private String password2;

    @NotEmpty(message = "필수 사항 입니다.")
    private String phoneNumber;

}
