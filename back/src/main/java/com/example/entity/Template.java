package com.example.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@ToString
@Document(collection="template")
public class Template implements Serializable {

    private Integer templateId;
    private String title;
    private String body;
    private String cover;

}
