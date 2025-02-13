package io.hexlet.javafxrepair;

import io.hexlet.javafxrepair.dto.RequestForm;
import io.hexlet.javafxrepair.dto.UpdateRequest;
import io.hexlet.javafxrepair.model.Request;
import java.sql.Date;

public class RequestMapper {
    public static Request toRequest(RequestForm requestForm) {
        Integer id = requestForm.getRequestNumber();
        Date startDate = requestForm.getStartDate();
        String type = requestForm.getType();
        String model = requestForm.getModel();
        String problem = requestForm.getProblemDescription();
        String status = requestForm.getStatus();
        return new Request(
                id,
                startDate,
                type,
                model,
                problem,
                status,
                null,
                "",
                null ,
                null
        );
    }

    public static Request toRequest(UpdateRequest updateRequest) {
        return new Request(
                updateRequest.getId(),
                updateRequest.getStartDate(),
                updateRequest.getType(),
                updateRequest.getModel(),
                updateRequest.getProblemDescription(),
                updateRequest.getStatus(),
                updateRequest.getFinishDate(),
                updateRequest.getRepairParts(),
                updateRequest.getMasterId(),
                updateRequest.getClientId()
        );
    }
}
