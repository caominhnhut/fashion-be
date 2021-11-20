package com.fashion.fashionbe.factory.mapper;

import java.util.function.Function;

import com.fashion.fashionbe.entity.UserEntity;

import com.fashion.fashionbe.dto.Account;
import com.fashion.fashionbe.enumeration.AuthorityName;
import com.fashion.fashionbe.model.Authority;

public class AccountMapper{

    private AccountMapper(){
        // hide constructor
    }

    public static Function<Account, com.fashion.fashionbe.model.Account> mapToModel = dto -> {

        com.fashion.fashionbe.model.Account model = new com.fashion.fashionbe.model.Account();
        model.setUserName(dto.getUserName());
        model.setPassword(dto.getPassword());
        Authority authority = new Authority();
        authority.setAuthorityName(AuthorityName.valueOf(dto.getRole().toString()));
        model.getAuthorities().add(authority);

        return model;
    };

    public  static Function<com.fashion.fashionbe.model.Account,UserEntity> mapToEntity = account -> {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(account.getUserName());
        userEntity.setPassword(account.getPassword());
        userEntity.setAuthorities(AuthorityMapper.mapToEntity.apply(account.getAuthorities()));

        return userEntity;
    };


}
