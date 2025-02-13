package io.hexlet.javafxrepair.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Comment {
    private Integer id;
    private String message;
    private Integer masterId;
    private Integer requestId;
}
