package com.Abhishek.Client.event.listener;

import com.Abhishek.Client.Service.UserService;
import com.Abhishek.Client.entity.User;
import com.Abhishek.Client.event.RegistrationCompletionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Slf4j
@Component
public class RegistrationCompletionEventListener implements
        ApplicationListener <RegistrationCompletionEvent> {

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompletionEvent event) {
        //create the verification token for user with link
        User user=event.getUser();
        String token= UUID.randomUUID().toString();
        userService.saveTokenForUser(token,user);
        //send the email
        String url=event.getAppUrl()+
                "/VerifyRegistration?token="+
                token;

        log.info("click on the link to verify your accout: {}",
                url);
    }
}
