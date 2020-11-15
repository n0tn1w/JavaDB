package com.spring.domain.dtos;

import com.spring.domain.entities.Game;
import com.spring.domain.entities.Order;
import com.spring.domain.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedUserDto {
    private String email;
    private String fullName;
    private Role role;
    private Set<Game> games;
    private Order order;
}
