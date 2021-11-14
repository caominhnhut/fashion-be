package com.fashion.fashionbe.model;

import com.fashion.fashionbe.enumeration.AuthorityName;

public class Authority{

    private AuthorityName authorityName;

    public AuthorityName getAuthorityName(){
        return authorityName;
    }

    public void setAuthorityName(AuthorityName authorityName){
        this.authorityName = authorityName;
    }
}
