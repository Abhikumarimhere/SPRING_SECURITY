package com.Abhishek.Client.Repository;

import com.Abhishek.Client.entity.PasswordResetToken;
import com.Abhishek.Client.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);



    PasswordResetToken findTokenByUser(User user);

    void deleteByToken(String token);
}
