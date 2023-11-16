package com.epam.service;

import com.epam.error.AccessException;
import com.epam.error.ErrorMessageConstants;
import com.epam.model.User;
import com.epam.model.dto.UserActivateDtoInput;
import com.epam.model.dto.UserDtoInput;
import com.epam.repo.UserRepo;
import com.epam.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final AuthenticationService authenticationService;

    @Value("${password.length}")
    private int passwordLength;

    @Override
    @Transactional
    public User save(UserDtoInput userDtoInput) {
        log.info("save, userDtoInput = {}", userDtoInput);

        return userRepo.save(createEntireUser(userDtoInput));
    }

    @Override
    public void changePassword(String userName, String oldPassword, String newPassword) {
        log.info("changePassword, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticationService.checkAccess(oldPassword, user)) {
            throw new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE);
        }

        user.setPassword(newPassword);
        userRepo.save(user);
    }

    @Override
    public void switchActivate(String userName, String password, UserActivateDtoInput userInput) {
        log.info("switchActivate, userName = {}", userName);

        User user = getUserByUserName(userName);
        if (authenticationService.checkAccess(password, user)) {
            throw new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE);
        }

        user.setIsActive(userInput.getIsActive());
        userRepo.save(user);
    }

    @Override
    public Optional<User> findUserByUsername(String userName) {
        if (userName.matches(".+-\\d$")) {
            String[] parts = userName.split("-");
            String userNameWithPostfix = parts[0].trim();
            Integer postfix = Integer.valueOf(parts[1]);
            return userRepo.findByUsernameAndPostfix(userNameWithPostfix, postfix);
        } else {
            return userRepo.findByUsernameAndPostfix(userName, 0);
        }
    }

    @Override
    public void login(String userName, String password) {
        log.info("changePassword, userName = {}", userName);
        User user = getUserByUserName(userName);

        if (authenticationService.checkAccess(password, user)) {
            throw new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE);
        }
    }

    private User getUserByUserName(String userName) {
        return userRepo.findByUsername(userName)
                       .orElseThrow(() -> new AccessException(ErrorMessageConstants.ACCESS_ERROR_MESSAGE));
    }


    public User createEntireUser(UserDtoInput userDtoInput) {
        String password = RandomStringGenerator.generateRandomString(passwordLength);
        String userName = userDtoInput.getFirstName().toLowerCase() + "." + userDtoInput.getLastName().toLowerCase();
        Integer maxPostfix = 0;

        if (isUserNameExistsInDatabase(userName)) {
            maxPostfix = userRepo.findMaxPostfixByUsername(userName);
            maxPostfix++;
        }

        return User.builder()
                   .firstName(userDtoInput.getFirstName())
                   .lastName(userDtoInput.getLastName())
                   .username(userName)
                   .password(password)
                   .postfix(maxPostfix)
                   .isActive(false)
                   .build();

    }

    public boolean isUserNameExistsInDatabase(String userName) {
        return userRepo.existsByUsername(userName);
    }
}
