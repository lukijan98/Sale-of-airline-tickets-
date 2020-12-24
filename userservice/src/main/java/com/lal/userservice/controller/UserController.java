package com.lal.userservice.controller;

import com.lal.userservice.model.ConfirmationToken;
import com.lal.userservice.model.User;
import com.lal.userservice.service.UserService;

import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;


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

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public void confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        userService.confirm(confirmationToken);
    }

}
