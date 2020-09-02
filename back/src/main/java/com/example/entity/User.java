package com.example.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
@Document(collection = "user")
public class User implements Serializable {

    @Id
    private String userId;
    private String name;
    private String pwd;
    private String mail;
    private String avatar;
    private String info;
    private List<Favor> favor;
    private List<Recent> recent;
    private List<Creation> creation;
}
