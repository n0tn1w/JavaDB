package com.spring.repositories;

import com.spring.domain.dtos.LoginUserDto;
import com.spring.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByEmailAndPassword(String email, String password);
}
