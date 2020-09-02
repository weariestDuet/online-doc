package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
public class Auth {

    private Integer groupWrite;
    private Integer groupDiscuss;
    private Integer otherRead;
    private Integer otherDiscuss;

    public Auth() {
        this.groupWrite = 1;
        this.groupDiscuss = 1;
        this.otherRead = 1;
        this.otherDiscuss = 0;
    }
}
