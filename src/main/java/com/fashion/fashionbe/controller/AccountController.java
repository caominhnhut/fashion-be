package com.fashion.fashionbe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fashion.fashionbe.enumeration.FieldName;
import com.fashion.fashionbe.exception.ValidationException;
import com.fashion.fashionbe.factory.mapper.AccountMapper;
import com.fashion.fashionbe.service.AccountService;

import om.fashion.fashionbe.dto.Account;
import om.fashion.fashionbe.dto.AccountId;
import om.fashion.fashionbe.dto.Problem;

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

@RestController
public class AccountController{

    @Autowired
    private AccountService accountService;

    @PostMapping("/authenticate/account")
    private ResponseEntity createAccount(@RequestBody Account accountDto){

        try{
            validate(accountDto);
        }catch(ValidationException e){

            Problem problem = new Problem();
            problem.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            problem.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            problem.setDetail(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        com.fashion.fashionbe.model.Account accountModel = AccountMapper.mapToModel(accountDto);

        Long id = accountService.create(accountModel);

        AccountId accountId = new AccountId();
        accountId.setId(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountId);
    }

    private void validate(Account account) throws ValidationException{

        isNotEmpty(account.getUserName(), FieldName.userName);

        isNotEmpty(account.getPassword(), FieldName.password);

    }

    private void isNotEmpty(String data, FieldName fieldName) throws ValidationException{
        if(data.length() == 0){
            throw new ValidationException(fieldName.getMessage());
        }
    }

    private void validateUserName(String userName){
        //TODO: username must follow email format
    }

    private void validatePassword(String password){
        //TODO: password must greater than or equal 8 letters
        //TODO: password must include UPPER-CASE, NUMBER and Special character
    }
}
