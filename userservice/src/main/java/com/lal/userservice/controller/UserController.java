package com.lal.userservice.controller;

import com.lal.userservice.model.User;
import com.lal.userservice.service.UserService;

import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;
import static com.lal.userservice.security.SecurityConstants.*;


@RestController
//@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value="/register",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User saveUser(@RequestBody User user){
        return userService.save(user);
    }

    @PostMapping(value="/update",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User user,@RequestHeader(value = HEADER_STRING) String token){
        return userService.update(user,token);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public void confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        userService.confirm(confirmationToken);
    }

}
