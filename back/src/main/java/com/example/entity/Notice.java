package com.example.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Document(collection="notice")
public class Notice {
    @Id
    private String noticeId;
    private String receiver;
    private String sender;
    private String action;
    private String item;
    private Date time;
    private Integer isRead;

    public Notice() { }

    public Notice(String receiver, String sender, String action, String item, Date time, Integer isRead) {
        this.receiver = receiver;
        this.sender = sender;
        this.action = action;
        this.item = item;
        this.time = time;
        this.isRead = isRead;
    }
}
