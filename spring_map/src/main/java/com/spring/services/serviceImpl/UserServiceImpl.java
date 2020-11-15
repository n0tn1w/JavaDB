package com.spring.services.serviceImpl;

import com.spring.domain.dtos.LoggedUserDto;
import com.spring.domain.dtos.LoginUserDto;
import com.spring.domain.dtos.RegisterUserDto;
import com.spring.domain.entities.Role;
import com.spring.domain.entities.User;
import com.spring.repositories.UserRepository;
import com.spring.services.GameService;
import com.spring.services.OrderService;
import com.spring.services.UserService;
import com.spring.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final ValidatorUtil validatorUtil;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private LoggedUserDto loggedUserDto;
    private final GameService gameService;
    private final OrderService orderService;

    @Autowired
    public UserServiceImpl(ValidatorUtil validatorUtil, UserRepository userRepository, ModelMapper modelMapper, GameService gameService, OrderService orderService) {
        this.validatorUtil = validatorUtil;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.gameService = gameService;
        this.orderService = orderService;
    }

    @Transactional
    @Override
    public String registerUser(RegisterUserDto userDto) {
        StringBuilder sb = new StringBuilder();

        if (userDto.getPassword().equals(userDto.getConfirmPassword())) {
            if (this.validatorUtil.isValid(userDto)) {
                User user = this.modelMapper.map(userDto, User.class);

                if (userRepository.count() == 0) {
                    user.setRole(Role.ADMIN);
                } else {
                    user.setRole(Role.USER);
                }

                sb.append(String.format("%s was registered %n", userDto.getFullName()));
                this.userRepository.saveAndFlush(user);

            } else {
                this.validatorUtil.violations(userDto).forEach(m -> {
                    sb.append(m.getMessage()).append(System.lineSeparator());
                });
            }

        }else {
            sb.append("Incorrect confirmation pass!");
        }
        return sb.toString().trim();
    }

    @Override
    public String loginUser(LoginUserDto userDto) {
        StringBuilder sb=new StringBuilder();

        Optional<User> userOptional=this.userRepository.findUserByEmailAndPassword(userDto.getEmail(),userDto.getPassword());

        if(!userOptional.isEmpty()){
            sb.append(String.format("Successfully logged in %s",userOptional.get().getFullName()));
            this.loggedUserDto=this.modelMapper.map(userOptional.get(), LoggedUserDto.class);
            gameService.setLoggedUser(this.loggedUserDto);
        }else {
            sb.append("Incorrect username/password!");
        }
        return sb.toString().trim();
    }

    @Override
    public String logOut() {
        StringBuilder sb=new StringBuilder();

        if(this.loggedUserDto!=null){
            sb.append(String.format("User %s successfully logged out",this.loggedUserDto.getFullName()));
            this.loggedUserDto=null;
        }else{
            sb.append("Cannot log out. No user was logged in.");
        }
        return sb.toString().trim();
    }
}
