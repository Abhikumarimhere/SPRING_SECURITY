package com.Abhishek.Client.Service;

import com.Abhishek.Client.entity.User;
import com.Abhishek.Client.entity.VerificationToken;
import com.Abhishek.Client.model.UserModel;

import java.util.Optional;

public interface UserService {
    User RegisterUser(UserModel user);

    void saveTokenForUser(String token, User user);

    String ValidateVerificationtoken(String token);

    VerificationToken generateVerificationToken(String token);

    User findUserByEmail(String email);

     void saveTokenForPR(User user,String token);

    String ValidatePasswordtoken(String token);


    Optional<User> getUserByToken(String token);

    void changePassword(User user,String newPassword);

    boolean validateOldPassword(User user, String oldPassword);


    void deleteIfExistingToken(User user);
}
