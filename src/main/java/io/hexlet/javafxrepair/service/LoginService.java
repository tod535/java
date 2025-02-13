package io.hexlet.javafxrepair.service;

import io.hexlet.javafxrepair.model.User;
import lombok.Getter;
import lombok.Setter;

public class LoginService {
    @Getter
    @Setter
    private static User currentUser;

    public static boolean authenticate(String username, String password) {
        try {
            User user = UserService.getUser(username);
            if (user.getPassword().equals(password)) {
                setCurrentUser(user);
                return true;
            } else
                throw new Exception();
        } catch (Exception e) {
            return false;
        }
    }
}