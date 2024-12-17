package com.Abhishek.Client.event;

import com.Abhishek.Client.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class RegistrationCompletionEvent extends ApplicationEvent {

    private User user;
    private String appUrl;
    public RegistrationCompletionEvent(User user,String appUrl) {
        super(user);
        this.appUrl=appUrl;
        this.user=user;
    }
}
