package com.lal.userservice.service;

import com.lal.userservice.model.User;

public interface UserService {

    User save(User user);
    User confirm(String confirmationToken);
    User update(User user,String token);
}
