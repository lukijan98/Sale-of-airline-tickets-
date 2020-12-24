package com.lal.userservice.service;

import com.lal.userservice.model.ConfirmationToken;
import com.lal.userservice.model.User;

public interface UserService {

    User save(User user);
    User confirm(String confirmationToken);
}
