package com.med.expsensetackerapi.services;

import com.med.expsensetackerapi.domain.User;
import com.med.expsensetackerapi.exceptions.EtAuthException;

public interface UserService {
    User validateUser(String email,String password) throws EtAuthException;
    User registerUser(String fName,String lName,String email,String password) throws EtAuthException;
}
