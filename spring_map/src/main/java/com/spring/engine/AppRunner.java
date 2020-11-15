package com.spring.engine;

import com.spring.domain.dtos.AddGameDto;
import com.spring.domain.dtos.LoginUserDto;
import com.spring.domain.dtos.RegisterUserDto;
import com.spring.services.GameService;
import com.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
public class AppRunner implements CommandLineRunner {

    private GameService gameService;
    private UserService userService;

    @Autowired
    public AppRunner(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        String line;

        while (!(line = bufferedReader.readLine()).equals("END")){
            String[] input=line.split("\\|");
            switch (input[0]){
                case "RegisterUser" :
                    RegisterUserDto registerUserDto=new RegisterUserDto(input[1],input[2],input[3],input[4]);
                    System.out.println(this.userService.registerUser(registerUserDto));
                    break;
                case "LoginUser":
                    LoginUserDto loginUserDto=new LoginUserDto(input[1],input[2]);
                    System.out.println(this.userService.loginUser(loginUserDto));
                    break;
                case "Logout":
                    System.out.println(this.userService.logOut());
                    break;
                case "AddGame":
                    String[] date= input[7].split("-");
                    String dateCorrect=date[2]+"-"+date[1]+"-"+date[0];
                    LocalDate localDate=LocalDate.parse(dateCorrect);
                    AddGameDto addGameDto=new AddGameDto(input[1], new BigDecimal(input[2]),Double.parseDouble(input[3]),input[4],input[5],input[6],localDate);
                    System.out.println(this.gameService.addGame(addGameDto));
                    break;
                case "EditGame":
                    System.out.println(this.gameService.editGame(line));
                    break;
                case "DeleteGame":
                    try {
                        System.out.println(this.gameService.deleteGame(Integer.parseInt(input[1])));
                    }catch (Exception e){
                        System.out.println("WRONG INPUT BRUV");
                    }
                    break;
                case "AllGames":
                    System.out.println(this.gameService.getAllGames());
                    break;
                case "DetailGame":
                    System.out.println(this.gameService.detailsGame(input[1]));
                    break;
                case "OwnedGames":
                    System.out.println(this.gameService.ownedGames());
                    break;
            }

        }

    }
}
