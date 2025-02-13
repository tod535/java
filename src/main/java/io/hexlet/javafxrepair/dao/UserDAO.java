package io.hexlet.javafxrepair.dao;

import io.hexlet.javafxrepair.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO extends BaseDAO {
    public static void updateUser(User user) {
        String sql = "UPDATE users SET (fio, phone, login, password, type) = (?, ?, ?, ?, ?) WHERE id =?";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getFio());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getLogin());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getType());
            ps.setInt(6, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<User> getUserByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            var rs = ps.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt("id");
                String phone = rs.getString("phone");
                String fio = rs.getString("fio");
                String password = rs.getString("password");
                String type = rs.getString("type");
                User user = new User(id, fio, phone, login, password, type);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public static Optional<User> getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String phone = rs.getString("phone");
                String fio = rs.getString("fio");
                String password = rs.getString("password");
                String type = rs.getString("type");
                String login = rs.getString("login");
                User user = new User(id, fio, phone, login, password, type);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public static List<User> getMasters() {
        String sql = "SELECT * FROM users WHERE type = ?";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Мастер");
            var rs = ps.executeQuery();
            List<User> masters = new ArrayList<>();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String phone = rs.getString("phone");
                String fio = rs.getString("fio");
                String password = rs.getString("password");
                String type = rs.getString("type");
                String login = rs.getString("login");
                masters.add(new User(id, fio, phone, login, password, type));
            }
            return masters;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
