package com.spring.services;

import com.spring.domain.dtos.AddGameDto;
import com.spring.domain.dtos.LoggedUserDto;
import com.spring.domain.entities.Game;

import java.util.Optional;

public interface GameService {
   String addGame(AddGameDto gameDto);
   String editGame(String input);
   String deleteGame(long id);

   String getAllGames();
   String detailsGame(String title);
   String ownedGames();

   void setLoggedUser(LoggedUserDto loggedUser);

   Optional<Game> getGameByTitle(String title);
}
