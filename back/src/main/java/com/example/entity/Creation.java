package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Creation {
    private String creationId;
    private String docId;
    private String title;

    public Creation(String docId, String title) {
        this.docId = docId;
        this.title = title;
    }
}
