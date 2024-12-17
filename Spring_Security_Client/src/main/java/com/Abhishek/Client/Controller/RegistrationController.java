package com.Abhishek.Client.Controller;

import com.Abhishek.Client.Service.UserService;
import com.Abhishek.Client.entity.User;
import com.Abhishek.Client.entity.VerificationToken;
import com.Abhishek.Client.event.RegistrationCompletionEvent;
import com.Abhishek.Client.model.PasswordModel;
import com.Abhishek.Client.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String RegisterUser(@RequestBody UserModel user, final HttpServletRequest request) {
        User user1 = userService.RegisterUser(user);
        publisher.publishEvent(new RegistrationCompletionEvent(
                user1,
                applicationUrl(request)
        ));
        return "Successfull";
    }

    @GetMapping("/VerifyRegistration")
    public String verificationToker(@RequestParam("token") String token) {
        String result = userService.ValidateVerificationtoken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User Registration Successfull";
        }
        return "Bad Used";
    }

    @GetMapping("/resend")
    public String ResendToken(@RequestParam("token") String token, HttpServletRequest request) {

        VerificationToken verificationToken = userService.generateVerificationToken(token);
        User user = verificationToken.getUser();
        ResendVerificationTokenMail(user, applicationUrl(request), verificationToken);
        return "verification link sent";

    }

    private void ResendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl +
                "/VerifyRegistration?token=" +
                verificationToken.getToken();

        log.info("click on the link to verify your accout: {}",
                url);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() + ":" +
                request.getServerPort() +
                request.getContextPath();
    }

    @PostMapping("/ResetPassword")
    public String Resetpassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if (user != null) {
            String token = UUID.randomUUID().toString();
//            userService.deleteIfExistingToken(user);
            userService.saveTokenForPR(user, token);
            sendResetMailToUser(user, applicationUrl(request), token);
            return "Reset Mail Sent";
        }
        return "Invalid user";
    }

    @PostMapping("/SavePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel) {
        String result = userService.ValidatePasswordtoken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "invalid";
        }

        Optional<User> user = userService.getUserByToken(token);
        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "password reset successfull";
        } else {
            return "unsuccessfull";
        }
    }

    private void sendResetMailToUser(User user, String appurl, String token) {
        String url = appurl + ":" +
                "/SavePassword?token=" +
                token;

        log.info("Please click on the link to save new password: {}", url);
    }

    @PostMapping("/changePassword")

    public String changePassword(@RequestBody PasswordModel passwordModel){
        User user=userService.findUserByEmail(passwordModel.getEmail());
        if(!userService.validateOldPassword(user,passwordModel.getOldPassword())){
            return "invalid request";
        }
        userService.changePassword(user,passwordModel.getNewPassword());
        return "Password Changed Successfully";
    }
}
