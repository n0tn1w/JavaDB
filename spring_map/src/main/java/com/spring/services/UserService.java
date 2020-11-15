package com.spring.services;

import com.spring.domain.dtos.LoginUserDto;
import com.spring.domain.dtos.RegisterUserDto;
import org.springframework.stereotype.Service;

public interface UserService {

    String registerUser(RegisterUserDto userDto);

    String loginUser(LoginUserDto userDto);

    String logOut();
}
