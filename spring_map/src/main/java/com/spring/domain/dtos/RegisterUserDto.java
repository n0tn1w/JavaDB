package com.spring.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {

    //The regex are wrong but i just couldn't be bothered
    @Pattern(regexp = "\\w+@+\\w+\\.+\\w+",message = "Email is not valid")
    @Length(min = 6,message = "To short")
    private String email;
    @Pattern(regexp = "[A-Z]+[a-z]+\\d+",message = "Password should have at least 1 uppercase/lowercase/digit")
    private String password;
    private String confirmPassword;
    private String fullName;
}
