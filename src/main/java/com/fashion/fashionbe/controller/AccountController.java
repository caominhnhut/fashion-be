package com.fashion.fashionbe.controller;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fashion.fashionbe.dto.Account;
import com.fashion.fashionbe.dto.AccountId;
import com.fashion.fashionbe.dto.AuthenticatedData;
import com.fashion.fashionbe.dto.Credential;
import com.fashion.fashionbe.dto.Problem;
import com.fashion.fashionbe.dto.Role;
import com.fashion.fashionbe.entity.UserEntity;
import com.fashion.fashionbe.enumeration.FieldName;
import com.fashion.fashionbe.exception.ValidationException;
import com.fashion.fashionbe.factory.TokenHelper;
import com.fashion.fashionbe.factory.mapper.AccountMapper;
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
     */

@RestController
public class AccountController{

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenHelper tokenHelper;

    private Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    private Predicate<String> validateEmailFormat = emailStr -> {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    };

    private Predicate<String> validatePassword = pwStr -> {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(pwStr);
        return matcher.find();
    };

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

        com.fashion.fashionbe.model.Account accountModel = AccountMapper.mapToModel.apply(accountDto);

        Long id = accountService.create(accountModel);

        AccountId accountId = new AccountId();
        accountId.setId(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountId);
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
            throw new ValidationException(fieldName.userName.getValidationMessage());
        }
    }

    private void validatePassword(String password, FieldName fieldName) throws ValidationException{
        if(!validatePassword.test(password)){
            throw new ValidationException(fieldName.password.getValidationMessage());
        }
    }

    @PostMapping(value = "/no-auth/login")
    public ResponseEntity authenticate(@RequestBody Credential credential)
    {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credential.getUserName(), credential.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        final String token = tokenHelper.generateToken(user.getUsername());

        AuthenticatedData authenticatedData = new AuthenticatedData();
        authenticatedData.setToken(token);
        authenticatedData.setExpiredIn(tokenHelper.getExpiresIn());
        return ResponseEntity.status(HttpStatus.OK).body(authenticatedData);
    }
}
