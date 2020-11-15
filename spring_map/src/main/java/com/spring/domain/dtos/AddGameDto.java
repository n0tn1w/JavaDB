package com.spring.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddGameDto {

    @Pattern(regexp = "[A-Z]+.+",message = "Title is not correct!")
    private String title;
    @DecimalMin(value = "0",message = "Price should be positive")
    private BigDecimal price;
    @Min(value = 0,message = "Size should be positive")
    private double size;
    @Length(min = 11,max = 11, message = "Not valid YT URL")
    private String trailer;
    //Da da kysmet
    private String imageThumbnail;
    @Length(min = 20,message = "The description is to short")
    private String description;
    private LocalDate releaseDate;
}
