package org.example.web.dto;

import lombok.Data;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Book {

    @NotNull
    private Integer id;

    @NotBlank
    private String author;

    @NotBlank
    private String title;

    @NotNull
    @Digits(integer = 4, fraction = 0)
    private Integer size;
}
