package com.spring.services.serviceImpl;

import com.spring.domain.dtos.LoggedUserDto;
import com.spring.domain.entities.Game;
import com.spring.domain.entities.Order;
import com.spring.repositories.OrderRepository;
import com.spring.services.GameService;
import com.spring.services.OrderService;
import com.spring.utils.ValidatorUtil;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(ModelMapper modelMapper, ValidatorUtil validatorUtil, OrderRepository orderRepository) {
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.orderRepository = orderRepository;
    }

}
