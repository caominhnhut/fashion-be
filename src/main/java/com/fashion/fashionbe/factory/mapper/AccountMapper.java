package com.fashion.fashionbe.factory.mapper;

import java.util.function.Function;

import com.fashion.fashionbe.entity.UserEntity;

import om.fashion.fashionbe.dto.Account;

public class AccountMapper{

    private AccountMapper(){
        // hide constructor
    }

    public static Function<Account, com.fashion.fashionbe.model.Account> mapToModel = dto -> {

        com.fashion.fashionbe.model.Account model = new com.fashion.fashionbe.model.Account();
        model.setUserName(dto.getUserName());
        model.setPassword(dto.getPassword());

        return model;
    };

    public static UserEntity mapToEntity(com.fashion.fashionbe.model.Account account){

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(account.getUserName());
        userEntity.setPassword(account.getPassword());

        return userEntity;
    }
}
