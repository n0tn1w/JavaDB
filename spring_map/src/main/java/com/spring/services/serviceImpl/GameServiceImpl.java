package com.spring.services.serviceImpl;

import com.spring.domain.dtos.AddGameDto;
import com.spring.domain.dtos.GameTitleAndPriceDto;
import com.spring.domain.dtos.LoggedUserDto;
import com.spring.domain.entities.Game;
import com.spring.domain.entities.Role;
import com.spring.repositories.GameRepository;
import com.spring.services.GameService;
import com.spring.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private LoggedUserDto loggedUserDto;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public String addGame(AddGameDto gameDto) {

        StringBuilder sb = new StringBuilder();
        if (adminCheck()!=null) {
            sb.append(adminCheck());
        } else {
            if (this.validatorUtil.isValid(gameDto)) {
                Game game = this.modelMapper.map(gameDto, Game.class);
                this.gameRepository.saveAndFlush(game);
                sb.append(String.format("Added %s", game.getTitle()));
            } else {
                this.validatorUtil.violations(gameDto).forEach(m -> {
                    sb.append(m.getMessage()).append(System.lineSeparator());
                });
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String editGame(String line) {
        StringBuilder sb = new StringBuilder();
        String[] input=line.split("\\|");
        Long id=Long.parseLong(input[1]);

        Optional<Game> game = gameRepository.findGameById(id);
        if (game.isEmpty()) {
            sb.append("Wrong id");
        } else {
            boolean isItValid=true;
            //title
            for (int i = 2; i <input.length ; i++) {
                if(input[i].split("=")[0].equals("title")){
                    game.get().setTitle(input[i].split("=")[1]);
                    if(validatorOfInputAndSavingIntoTheDB(game)){
                        isItValid=false;
                    }
                }
            }
            //price
            for (int i = 2; i <input.length ; i++) {
                if(input[i].split("=")[0].equals("price")){
                    BigDecimal price=new BigDecimal(input[i].split("=")[1]);
                    game.get().setPrice(price);
                    if(validatorOfInputAndSavingIntoTheDB(game)){
                        isItValid=false;

                        //obi4am pastet :)
                    }
                }
            }
            //size
            for (int i = 2; i <input.length ; i++) {
                if(input[i].split("=")[0].equals("size")){
                    game.get().setSize(Double.parseDouble(input[i].split("=")[1]));
                    if(validatorOfInputAndSavingIntoTheDB(game)){
                        isItValid=false;
                    }
                }
            }
            //trailer
            for (int i = 2; i <input.length ; i++) {
                if(input[i].split("=")[0].equals("trailer")){
                    game.get().setTrailer(input[i].split("=")[1]);
                    if(validatorOfInputAndSavingIntoTheDB(game)){
                        isItValid=false;
                    }
                }
            }
            //thumbnail
            for (int i = 2; i <input.length ; i++) {
                if(input[i].split("=")[0].equals("thumbnail")){
                    game.get().setImageThumbnail(input[i].split("=")[1]);
                    if(validatorOfInputAndSavingIntoTheDB(game)){
                        isItValid=false;
                    }
                }
            }
            //description
            for (int i = 2; i <input.length ; i++) {
                if(input[i].split("=")[0].equals("description")){
                    game.get().setDescription(input[i].split("=")[1]);
                    if(validatorOfInputAndSavingIntoTheDB(game)){
                        isItValid=false;
                    }
                }
            }

            if(isItValid){
                sb.append(String.format("Edited %s",game.get().getTitle()));
            }else {
                return "Invalid parameter the hole query was terminated!";
            }


        }
        return sb.toString().trim();
    }

    @Override
    public String deleteGame(long id) {
        StringBuilder sb = new StringBuilder();
        if (adminCheck()!=null) {
          sb.append(adminCheck());
        } else {
            Optional<Game> game = gameRepository.findGameById(id);
            if (game.isEmpty()) {
                sb.append("Wrong id");
            } else {
                gameRepository.deleteById(id);
                sb.append(String.format("Deleted %s", game.get().getTitle()));
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String getAllGames() {
        StringBuilder sb=new StringBuilder();
        if(this.gameRepository.findAll().isEmpty()){
            return "No games in store";
        }

        List<Game> allGames = this.gameRepository.findAll();
        List<GameTitleAndPriceDto> games=new LinkedList<>();
        for (Game fullGame : allGames) {
            GameTitleAndPriceDto newGame = this.modelMapper.map(fullGame, GameTitleAndPriceDto.class);
            games.add(newGame);
            sb.append(String.format("%s %.2f%n",newGame.getTitle(),newGame.getPrice()));
        }
        return sb.toString().trim();
    }

    @Override
    public String detailsGame(String title) {
        StringBuilder sb=new StringBuilder();

        Optional<Game> game = gameRepository.findGameByTitle(title);
        if (game.isEmpty()) {
            sb.append("Wrong id");
        }else{
            sb.append(String.format("Title: %s",game.get().getTitle())).append(System.lineSeparator());
            sb.append(String.format("Price: %.2f",game.get().getPrice())).append(System.lineSeparator());
            sb.append(String.format("Price: %s",game.get().getDescription())).append(System.lineSeparator());
            sb.append(String.format("Release date: %s",game.get().getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        }
        return sb.toString().trim();
    }

    @Override
    public String ownedGames() {
        StringBuilder sb=new StringBuilder();
        if(loggedUserDto==null){
            return "No logged user";
        }
        Set<Game> games = loggedUserDto.getGames();
        if(games.isEmpty()){
            sb.append("You have no games!");
        }else{
            for (Game game : games) {
                sb.append(game.getTitle()).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }


    @Override
    public void setLoggedUser(LoggedUserDto loggedUser) {
        this.loggedUserDto = loggedUser;
    }

    @Override
    public Optional<Game> getGameByTitle(String title) {
        return this.gameRepository.findGameByTitle(title);
    }

    private boolean validatorOfInputAndSavingIntoTheDB(Optional<Game> game) {
        Game g=game.get();
        AddGameDto addGameDto=new AddGameDto(g.getTitle(),g.getPrice(),g.getSize(),g.getTrailer(),g.getImageThumbnail(),g.getDescription(),g.getReleaseDate());
        if (this.validatorUtil.isValid(addGameDto)) {
            this.gameRepository.saveAndFlush(game.get());
            return false;
        }else {
            return true;
        }
    }

    private String adminCheck() {
        StringBuilder sb = new StringBuilder();

        if (this.loggedUserDto == null) {
            sb.append("No user logged in the system");

        } else if (this.loggedUserDto.getRole() == Role.USER) {

            sb.append("This user doesnt have an ADMIN role");
        }else {
            return null;
        }
        return sb.toString().trim();
    }



}
