package com.fashion.fashionbe.service;

import com.fashion.fashionbe.model.Account;

public interface AccountService{

    Long create(Account account);

    boolean update(Account account);
}
