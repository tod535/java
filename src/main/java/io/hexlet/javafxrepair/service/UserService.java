package io.hexlet.javafxrepair.service;

import io.hexlet.javafxrepair.dao.UserDAO;
import io.hexlet.javafxrepair.model.Request;
import io.hexlet.javafxrepair.model.User;

import java.util.List;

public class UserService {
    public static User getUser(Request request) {
        return UserDAO.getUserById(request.getClientId())
                .orElseThrow(RuntimeException::new);
    }
    public static User getUser(String login) {
        return UserDAO.getUserByLogin(login)
                .orElseThrow(RuntimeException::new);
    }

    public static List<User> getMasters() {
        return UserDAO.getMasters();
    }

    public static void updateUser(User user) {
        UserDAO.updateUser(user);
    }
}
