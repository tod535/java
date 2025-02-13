package io.hexlet.javafxrepair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class RequestForm {
    private Integer requestNumber;
    private Date startDate;
    private String type;
    private String model;
    private String problemDescription;
    private String fullName;
    private String phone;
    private String status;
}
