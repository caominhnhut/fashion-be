package com.fashion.fashionbe.repository;

import org.springframework.data.repository.CrudRepository;

import com.fashion.fashionbe.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>{

}
