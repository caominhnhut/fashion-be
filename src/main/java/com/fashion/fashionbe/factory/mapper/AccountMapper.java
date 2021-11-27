package com.fashion.fashionbe.factory.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fashion.fashionbe.dto.Account;
import com.fashion.fashionbe.dto.Role;
import com.fashion.fashionbe.entity.UserEntity;
import com.fashion.fashionbe.enumeration.AuthorityName;
import com.fashion.fashionbe.model.Authority;

public class AccountMapper{

    private static Function<List<Role>, List<Authority>> mapToAuthorityList = roles -> roles.stream().map(role -> {
        Authority authority = new Authority();
        authority.setAuthorityName(AuthorityName.valueOf(role.toString()));
        return authority;
    }).collect(Collectors.toList());

    public static final Function<Account, com.fashion.fashionbe.model.Account> mapToModel = dto -> {

        com.fashion.fashionbe.model.Account model = new com.fashion.fashionbe.model.Account();
        model.setUserName(dto.getUserName());
        model.setPassword(dto.getPassword());
        model.getAuthorities().addAll(mapToAuthorityList.apply(dto.getRoles()));
        return model;
    };

    private static Function<List<Authority>, List<com.fashion.fashionbe.entity.Authority>> mapToAuthorityEntity = authorities -> authorities.stream().map(authority -> {
        com.fashion.fashionbe.entity.Authority authorityEntity = new com.fashion.fashionbe.entity.Authority();
        authorityEntity.setName(authority.getAuthorityName());
        return authorityEntity;
    }).collect(Collectors.toList());

    public static final Function<com.fashion.fashionbe.model.Account, UserEntity> mapToEntity = account -> {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(account.getUserName());
        userEntity.setPassword(account.getPassword());
        userEntity.setAuthorities(mapToAuthorityEntity.apply(account.getAuthorities()));

        return userEntity;
    };

    private AccountMapper(){
        // hide constructor
    }
}
