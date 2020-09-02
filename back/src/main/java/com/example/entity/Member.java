package com.example.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Member {

    private String name;
    private String avatar;
    private String mail;

    public Member() {}

    public Member(String name, String avatar, String mail) {
        this.name = name;
        this.avatar = avatar;
        this.mail = mail;
    }
}
