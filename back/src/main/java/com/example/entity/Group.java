package com.example.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@ToString
@Document(collection="group")
public class Group {

    @Id
    private String groupId;
    private String name;
    private Member admin;
    private List<Member> member;
    private List<Creation> creation;

    public Group() {}

    public Group(String name, Member admin, List<Member> member) {
        this.name = name;
        this.admin = admin;
        this.member = member;
    }
}
