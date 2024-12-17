package com.Abhishek.Client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordModel {

    private String newPassword;
    private String oldPassword;
    private String email;
}
