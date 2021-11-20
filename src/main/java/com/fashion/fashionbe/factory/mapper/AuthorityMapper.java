package com.fashion.fashionbe.factory.mapper;

import com.fashion.fashionbe.model.Authority;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AuthorityMapper {
    public static Function<List<Authority>, List<com.fashion.fashionbe.entity.Authority>> mapToEntity = authorities ->{
        List<com.fashion.fashionbe.entity.Authority> authoritiesEntity = new ArrayList<>();

        for(Authority authority: authorities){
            com.fashion.fashionbe.entity.Authority authorityEntity = new com.fashion.fashionbe.entity.Authority();
            authorityEntity.setName(authority.getAuthorityName());
            authoritiesEntity.add(authorityEntity);
        }

        return authoritiesEntity;
    };
}
