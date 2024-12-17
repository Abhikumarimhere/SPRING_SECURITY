package com.Abhishek.Client.Service;

import com.Abhishek.Client.Repository.PasswordTokenRepository;
import com.Abhishek.Client.Repository.UserRepository;
import com.Abhishek.Client.Repository.VerificationTokenRepository;
import com.Abhishek.Client.entity.PasswordResetToken;
import com.Abhishek.Client.entity.User;
import com.Abhishek.Client.entity.VerificationToken;
import com.Abhishek.Client.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Override
    public User RegisterUser(UserModel user) {
        User user1=new User();
        user1.setEmailId(user.getEmailId());
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setRole("USER");
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user1);
        return user1;
    }

    @Override
    public void saveTokenForUser(String token, User user) {
        VerificationToken verificationToken=new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String ValidateVerificationtoken(String token) {
        VerificationToken verificationToken=verificationTokenRepository.findByToken(token);
        if(verificationToken==null){
            return "invalid";
        }
        User user=verificationToken.getUser();
        Calendar cal= Calendar.getInstance();
        if(verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime() <=0){
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";

    }

    @Override
    public VerificationToken generateVerificationToken(String oldtoken) {
        VerificationToken verificationToken=verificationTokenRepository.findByToken(oldtoken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        User user=userRepository.findByEmailId(email);
        return user;
    }

    @Override
    public void saveTokenForPR(User user,String token) {
        PasswordResetToken passwordResetToken=new PasswordResetToken(user,token);
        passwordTokenRepository.save(passwordResetToken);
    }

    @Override
    public String ValidatePasswordtoken(String token) {
        PasswordResetToken passwordResetToke=passwordTokenRepository.findByToken(token);
        if(passwordResetToke==null){
            return "invalid";
        }
        User user=passwordResetToke.getUser();
        Calendar cal=Calendar.getInstance();
        if(passwordResetToke.getExpirationTime().getTime()
        - cal.getTime().getTime()<=0) {
            passwordTokenRepository.delete(passwordResetToke);
            return "Expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByToken(String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user,String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean validateOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword,user.getPassword());
    }

    @Override
    public void deleteIfExistingToken(User user) {
        PasswordResetToken passwordResetToken=
                passwordTokenRepository.findTokenByUser(user);
        log.info("pwd token: {}",passwordResetToken);
        passwordTokenRepository.deleteById(passwordResetToken.getId());
    }
}
