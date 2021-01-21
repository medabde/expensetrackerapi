package com.med.expsensetackerapi.repositories;

import com.med.expsensetackerapi.domain.User;
import com.med.expsensetackerapi.exceptions.EtAuthException;

public interface UserRepository {
    Integer create(String fName,String lName,String email,String password) throws EtAuthException;
    User findByEmailAndPassword(String email, String password) throws EtAuthException;
    Integer getCountByEmail(String email);
    User findById(int userId);

}
