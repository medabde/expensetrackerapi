package com.med.expsensetackerapi.services;

import com.med.expsensetackerapi.domain.User;
import com.med.expsensetackerapi.exceptions.EtAuthException;
import com.med.expsensetackerapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements  UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws EtAuthException {
        return null;
    }

    @Override
    public User registerUser(String fName, String lName, String email, String password) throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(email!=null) email = email.toLowerCase();
        if(!pattern.matcher(email).matches()){
            throw new EtAuthException("Invalid email format");
        }
        int count = userRepository.getCountByEmail(email);
        if (count>0) throw new EtAuthException("Email already in use");
        int userId = userRepository.create(fName, lName, email, password);
        return userRepository.findById(userId);
    }
}
