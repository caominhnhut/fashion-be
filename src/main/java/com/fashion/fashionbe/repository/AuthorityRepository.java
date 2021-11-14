package com.fashion.fashionbe.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fashion.fashionbe.entity.Authority;
import com.fashion.fashionbe.enumeration.AuthorityName;

public interface AuthorityRepository extends CrudRepository<Authority, Long>{

    List<Authority> findByName(AuthorityName authorityName);
}
