package com.spring.repositories;

import com.spring.domain.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {
    Optional<Game> findGameById(long id);
    Optional<Game> findGameByTitle(String title);
}
