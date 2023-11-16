package com.epam.service;

import com.epam.model.User;
import com.epam.model.dto.UserActivateDtoInput;
import com.epam.model.dto.UserDtoInput;

import java.util.Optional;

public interface UserService {

    User save(UserDtoInput userDtoInput);

    void changePassword(String userName, String oldPassword, String newPassword);

    void switchActivate(String userName, String password, UserActivateDtoInput userInput);

    Optional<User> findUserByUsername(String userName);

    void login(String userName, String password);
}
