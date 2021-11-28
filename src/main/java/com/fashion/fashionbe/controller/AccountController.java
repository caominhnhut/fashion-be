package com.fashion.fashionbe.controller;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fashion.fashionbe.dto.Account;
import com.fashion.fashionbe.dto.AccountData;
import com.fashion.fashionbe.dto.AccountId;
import com.fashion.fashionbe.dto.Role;
import com.fashion.fashionbe.enumeration.FieldName;
import com.fashion.fashionbe.exception.ValidationException;
import com.fashion.fashionbe.factory.CommonUtility;
import com.fashion.fashionbe.factory.mapper.AccountMapper;
import com.fashion.fashionbe.model.Authority;
import com.fashion.fashionbe.service.AccountService;

/*
    FE ----> DTO(Account) BE (Controller)
    Controller: Recive requestion, return Response
    Service: Handle logic
    Repository: DB
    ---
    FE ----> DTO (target) <---- BE
    Controller ----> Model <----- Service
    Service ----> Entity <------ Repository

    restfull

resources => account

POST   --> create   --> domain/account
PUT    --> update   --> domain/account/account-id
DELETE --> delete   --> domain/account/account-id
GET    --> read     --> domain/account/account-id or domain/accounts
----

Quan ao (1)
+ Ao so mi (3)
+ Ao thun

Giay Dep (2)
+ Nike
+ Adidas

endpoint regarding product
POST   --> create   --> domain/category/cat-id/product              ---->  domain/category/1/product
PUT    --> update   --> domain/category/cat-id/product/pid          ---->  domain/category/1/product/3
DELETE --> delete   --> domain/category/cat-id/product/pid          ---->  domain/category/1/product/3
GET    --> read     --> domain/category/cat-id/product/pid or domain/category/cat-id/products ---->  domain/category/1/products
     */

@RestController
public class AccountController{

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private AccountService accountService;

    @Autowired
    private CommonUtility commonUtility;

    private Predicate<String> validateEmailFormat = emailStr -> {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    };

    private Predicate<String> validatePassword = pwStr -> {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(pwStr);
        return matcher.find();
    };

    @PostMapping("/no-auth/account")
    public ResponseEntity createAccount(@RequestBody Account accountDto){

        try{
            validate(accountDto);
        }catch(ValidationException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonUtility.createProblem.apply(e.getMessage()));
        }

        com.fashion.fashionbe.model.Account accountModel = AccountMapper.mapToModel.apply(accountDto);

        Long id = accountService.create(accountModel);

        AccountId accountId = new AccountId();
        accountId.setId(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountId);
    }

    @PutMapping("/account/{account-id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity updateAccount(@PathVariable("account-id") Long accountId, @RequestBody AccountData accountDataDto){

        try{
            isRolesNotEmpty(accountDataDto.getRoles());
        }catch(ValidationException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonUtility.createProblem.apply(e.getMessage()));
        }

        List<Authority> authorities = AccountMapper.mapToAuthorityList.apply(accountDataDto.getRoles());
        com.fashion.fashionbe.model.Account account = new com.fashion.fashionbe.model.Account();
        account.setId(accountId);
        account.getAuthorities().addAll(authorities);

        return ResponseEntity.status(HttpStatus.OK).body(accountService.update(account));
    }

    private void validate(Account account) throws ValidationException{

        isNotEmpty(account.getUserName(), FieldName.userName);

        isNotEmpty(account.getPassword(), FieldName.password);

        isRolesNotEmpty(account.getRoles());

        validateUserName(account.getUserName(), FieldName.userName);

        validatePassword(account.getPassword(), FieldName.password);
    }

    private void isNotEmpty(String data, FieldName fieldName) throws ValidationException{
        if(data.length() == 0){
            throw new ValidationException(fieldName.getEmptyMessage());
        }
    }

    private void isRolesNotEmpty(List<Role> roles) throws ValidationException{
        if(roles.isEmpty()){
            throw new ValidationException(FieldName.roles.getEmptyMessage());
        }
    }

    private void validateUserName(String userName, FieldName fieldName) throws ValidationException{
        if(!validateEmailFormat.test(userName)){
            throw new ValidationException(fieldName.getValidationMessage());
        }
    }

    private void validatePassword(String password, FieldName fieldName) throws ValidationException{
        if(!validatePassword.test(password)){
            throw new ValidationException(fieldName.getValidationMessage());
        }
    }
}
