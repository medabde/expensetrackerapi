package com.med.expsensetackerapi.repositories;

import com.med.expsensetackerapi.domain.User;
import com.med.expsensetackerapi.exceptions.EtAuthException;

public interface UserRepository {
    int create(String fName,String lName,String email,String password) throws EtAuthException;
    User findByEmailAndPassword(String email, String password) throws EtAuthException;
    int getCountByEmail(String email);
    User findById(int userId);

}
