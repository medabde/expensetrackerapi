package com.med.expsensetackerapi.resources;


import com.med.expsensetackerapi.Constants;
import com.med.expsensetackerapi.domain.User;
import com.med.expsensetackerapi.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody Map<String, Object> userMap){
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        User user = userService.validateUser(email,password);

        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(@RequestBody Map<String,Object> userMap){
        String fName = (String) userMap.get("fname");
        String lName = (String) userMap.get("lname");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        User user = userService.registerUser(fName,lName,email,password);


        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    private Map<String,String> generateJWTToken(User user){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp+Constants.TOKEN_VALIDITY))
                .claim("userId",user.getId())
                .claim("email",user.getEmail())
                .claim("firstname",user.getfName())
                .claim("lastname",user.getlName())
                .compact();
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        return map;
    }

}
