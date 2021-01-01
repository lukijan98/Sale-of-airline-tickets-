package com.lal.userservice.controller;

import com.lal.userservice.model.CreditCard;
import com.lal.userservice.model.User;
import com.lal.userservice.service.UserService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping(value="/addcreditcard",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addCreditCard(@RequestBody CreditCard creditCard, @RequestHeader(value = HEADER_STRING) String token){
         userService.addCreditCard(creditCard,token);
    }

    @PostMapping(value="/updatemilesandrank",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@RequestBody int miles,@RequestHeader(value = HEADER_STRING) String token){
        userService.updateMilesAndRank(miles,token);
    }

    @PostMapping(value = "/userInfo",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userInfo(@RequestHeader Long userId){
        Optional<User> optionalUser = userService.findUserById(userId);
        List<CreditCard> creditCards = null;
        String rank = null;
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            creditCards = user.getCreditCards();
            rank = user.getRank();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("creditCards",creditCards);
        map.put("rank",rank);
        return new ResponseEntity<Map<String,Object>>(map,new HttpHeaders(), HttpStatus.OK);
    }

}
