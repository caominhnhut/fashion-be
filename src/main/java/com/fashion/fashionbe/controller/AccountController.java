package com.fashion.fashionbe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fashion.fashionbe.factory.mapper.AccountMapper;
import com.fashion.fashionbe.service.AccountService;

import om.fashion.fashionbe.dto.Account;
import om.fashion.fashionbe.dto.AccountId;

@RestController
public class AccountController{

    @Autowired
    private AccountService accountService;

    @PostMapping("/authenticate/account")
    private ResponseEntity createAccount(@RequestBody Account accountDto){

        com.fashion.fashionbe.model.Account accountModel = AccountMapper.mapToModel(accountDto);

        Long id = accountService.create(accountModel);

        AccountId accountId = new AccountId();
        accountId.setId(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountId);
    }

    /*
    FE ----> DTO(Account) BE (Controller)
    Controller: Recive requestion, return Response
    Service: Handle logic
    Repository: DB
    ---
    FE ----> DTO (target) <---- BE
    Controller ----> Model <----- Service
    Service ----> Entity <------ Repository
     */
}
