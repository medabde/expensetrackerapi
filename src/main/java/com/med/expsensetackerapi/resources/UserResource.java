package com.med.expsensetackerapi.resources;


import com.med.expsensetackerapi.domain.User;
import com.med.expsensetackerapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(@RequestBody Map<String,Object> userMap){
        String fName = (String) userMap.get("fName");
        String lName = (String) userMap.get("lName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        User user = userService.registerUser(fName,lName,email,password);
        Map<String,String> map = new HashMap<>();
        map.put("message","registered successfully");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
