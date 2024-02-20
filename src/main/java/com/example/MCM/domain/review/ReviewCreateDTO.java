package com.example.MCM.domain.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateDTO {

    @NotBlank(message = "제목을 확인해 주세요.")
    private String title;

    @NotBlank(message = "내용을 확인해 주세요.")
    private String content;

    @NotNull(message = "별점을 선택해 주세요.")
    private double starScore;
}