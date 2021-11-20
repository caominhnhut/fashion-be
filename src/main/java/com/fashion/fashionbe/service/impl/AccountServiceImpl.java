package com.fashion.fashionbe.service.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashion.fashionbe.entity.Authority;
import com.fashion.fashionbe.entity.UserEntity;
import com.fashion.fashionbe.enumeration.AuthorityName;
import com.fashion.fashionbe.factory.CommonUtility;
import com.fashion.fashionbe.factory.mapper.AccountMapper;
import com.fashion.fashionbe.model.Account;
import com.fashion.fashionbe.repository.AuthorityRepository;
import com.fashion.fashionbe.repository.UserRepository;
import com.fashion.fashionbe.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Long create(Account account){

        UserEntity userEntity = AccountMapper.mapToEntity.apply(account);

        List<Authority> authorities = findAuthoritiesByName.apply(userEntity.getRoles());
        userEntity.setAuthorities(authorities);

        String encodedPassword = commonUtility.passwordEncoder().encode(account.getPassword());
        userEntity.setPassword(encodedPassword);

        UserEntity createdUser = userRepository.save(userEntity);

        return createdUser.getId();
    }

    private Function<List<Authority>, List<Authority>> findAuthoritiesByName = authorities -> {

        List<Authority> result = authorities.stream().map(authority -> {
            List<Authority> subAuthority = authorityRepository.findByName(authority.getName());
            return subAuthority.get(0);
        }).collect(Collectors.toList());

        if(result.isEmpty()){
            throw new IllegalArgumentException(String.format("The role [%s] not found", AuthorityName.ROLE_CUSTOMER.name()));
        }

        return result;
    };
}
