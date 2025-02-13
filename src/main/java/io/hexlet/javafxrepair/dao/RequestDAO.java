package io.hexlet.javafxrepair.dao;

import io.hexlet.javafxrepair.model.Comment;
import io.hexlet.javafxrepair.model.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO extends BaseDAO {
    public static ObservableList<Request> getRequests() {
        String sql = "SELECT * FROM requests";
        List<Request> requests = new ArrayList<>();
        try (var ps = connection.prepareStatement(sql)) {
            var rs = ps.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("id");
                Date date = rs.getDate("start_date");
                String type = rs.getString("org_tech_type");
                String model = rs.getString("org_tech_model");
                String description = rs.getString("problem_description");
                String status = rs.getString("request_status");
                Date finish = rs.getDate("completion_date");
                String repairParts = rs.getString("repair_parts");
                Integer master = rs.getInt("masterID") == 0 ? null : rs.getInt("masterID");
                Integer client = rs.getInt("clientID");
                Request request = new Request(
                        id,
                        date,
                        type,
                        model,
                        description,
                        status,
                        finish,
                        repairParts,
                        master,
                        client
                );
                requests.add(request);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return FXCollections.observableList(requests);
    }

    public static List<Comment> getRequestComments(Integer requestId) {
        String sql = "SELECT * FROM comments WHERE requestid = ?";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            var rs = ps.executeQuery();
            List<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                Integer commentId = rs.getInt("id");
                String message = rs.getString("message");
                Integer masterId = rs.getInt("masterid");
                comments.add(new Comment(commentId, message, masterId, requestId));
            }
            return comments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addRequest(Request request) {
        String sql = "INSERT INTO requests VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setInt(1, request.getId());
            ps.setDate(2, request.getStartDate());
            ps.setString(3, request.getType());
            ps.setString(4, request.getModel());
            ps.setString(5, request.getProblemDescription());
            ps.setString(6, request.getStatus());
            ps.setDate(7, request.getFinishDate());
            ps.setString(8, request.getRepairParts());
            ps.setObject(9, request.getMasterId());
            ps.setInt(10, request.getClientId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isIdAvailable(int id) {
        String sql = "SELECT (id) FROM requests WHERE id=?";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            var rs = ps.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void save(Request request) {
        String sql = "UPDATE requests " +
                "SET (org_tech_type," +
                " org_tech_model," +
                " problem_description," +
                " request_status," +
                " completion_date," +
                " repair_parts," +
                " masterID) = (?, ?, ?, ?, ?, ?, ?) " +
                "WHERE id = ?";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setString(1, request.getType());
            ps.setString(2, request.getModel());
            ps.setString(3, request.getProblemDescription());
            ps.setString(4, request.getStatus());
            ps.setDate(5, request.getFinishDate());
            ps.setString(6, request.getRepairParts());
            ps.setObject(7, request.getMasterId());
            ps.setInt(8, request.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveComment(Comment comment) {
        String sql = "INSERT INTO comments (message, masterid, requestid) VALUES (?, ?, ?)";
        try (var ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, comment.getMessage());
            ps.setInt(2, comment.getMasterId());
            ps.setInt(3, comment.getRequestId());
            ps.executeUpdate();
            var id = ps.getGeneratedKeys();
            if (id.next()) {
                comment.setId(id.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
