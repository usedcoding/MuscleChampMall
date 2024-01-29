package com.example.MCM.domain.email;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailDto {

    @NotBlank(message = "이메일을 확인해 주세요")
    private String email;

    @NotBlank(message = "전화번호를 확인해 주세요")
    private String phoneNumber;

    @NotBlank(message = "아이디를 확인해 주세요")
    private String username;
}
