package com.opitral.diaryapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecordDto {
    @Null
    private Long id;

    @NotNull
    @Size(min = 10, max = 5000)
    private String content;

    @PastOrPresent
    private LocalDate date;
}