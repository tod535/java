package io.hexlet.javafxrepair.service;

import io.hexlet.javafxrepair.RequestMapper;
import io.hexlet.javafxrepair.dao.RequestDAO;
import io.hexlet.javafxrepair.dto.RequestForm;
import io.hexlet.javafxrepair.dto.UpdateRequest;
import io.hexlet.javafxrepair.model.Comment;
import io.hexlet.javafxrepair.model.Request;
import io.hexlet.javafxrepair.model.User;

import java.util.List;

public class RequestService {
    public static void saveRequest(RequestForm requestForm) throws Exception {
        User user = LoginService.getCurrentUser();
        Request request = RequestMapper.toRequest(requestForm);
        request.setClientId(user.getId());
        user.setFio(requestForm.getFullName());
        user.setPhone(requestForm.getPhone());
        if (RequestDAO.isIdAvailable(requestForm.getRequestNumber())) {
            UserService.updateUser(user);
            RequestDAO.addRequest(request);
        } else throw new Exception("Заявка с таким номером уже сущесвует.");
    }

    public static void updateRequest(UpdateRequest updateRequest) {
        Request request = RequestMapper.toRequest(updateRequest);
        User user = UserService.getUser(request);
        user.setFio(updateRequest.getFullName());
        user.setPhone(updateRequest.getPhone());
        RequestDAO.save(request);
        UserService.updateUser(user);
    }

    public static List<Comment> getComments(Integer requestId) {
        return RequestDAO.getRequestComments(requestId);
    }

    public static void saveComment(Comment comment) {
        RequestDAO.saveComment(comment);
    }
}
