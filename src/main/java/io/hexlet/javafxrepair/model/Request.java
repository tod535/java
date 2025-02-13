package io.hexlet.javafxrepair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request {
    private Integer id;
    private Date startDate;
    private String type;
    private String model;
    private String problemDescription;
    private String status;
    private Date finishDate;
    private String repairParts;
    private Integer masterId;
    private Integer clientId;
}
