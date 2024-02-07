package com.example.MCM.domain.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailDto {

    @NotEmpty(message = "이메일을 확인해 주세요")
    private String email;

    @NotEmpty(message = "전화번호를 확인해 주세요")
    private String phoneNumber;

    @NotEmpty(message = "아이디를 확인해 주세요")
    private String username;
}
