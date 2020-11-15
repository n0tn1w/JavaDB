package com.spring.config;

import com.spring.utils.ValidatorUtil;
import com.spring.utils.ValidatorUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper(){return  new ModelMapper();}

    @Bean
    public Validator validator(){return Validation.buildDefaultValidatorFactory().getValidator();}

    @Bean
    public ValidatorUtil validatorUtil(){return  new ValidatorUtilImpl(validator());}

}
