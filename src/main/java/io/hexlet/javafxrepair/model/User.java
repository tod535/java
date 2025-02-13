package io.hexlet.javafxrepair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Integer id;
    private String fio;
    private String phone;
    private String login;
    private String password;
    private String type;
}
