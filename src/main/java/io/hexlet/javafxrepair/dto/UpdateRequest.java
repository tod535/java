package io.hexlet.javafxrepair.dto;

import io.hexlet.javafxrepair.model.Request;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class UpdateRequest {
    private final Integer id;
    private final Date startDate;
    private final String type;
    private final String model;
    private final String problemDescription;
    private final String status;
    private final Integer clientId;
    private final Date finishDate;
    private final String repairParts;
    private final Integer masterId;

    private String fullName;
    private String phone;

    public UpdateRequest(Request request) {
        id = request.getId();
        startDate = request.getStartDate();
        type = request.getType();
        model = request.getModel();
        problemDescription = request.getProblemDescription();
        status = request.getStatus();
        clientId = request.getClientId();
        finishDate = request.getFinishDate();
        repairParts = request.getRepairParts();
        masterId = request.getMasterId();
    }
}
