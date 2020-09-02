package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
@Document(collection="doc")
public class Doc {

	@Id
    private String docId;
    private String title;
    private String body;
    private Date time;
    private String userId;
    private String groupId;
	private List<Discuss> discuss;
	private Auth auth;

	public Doc(String title,String body,Date time,String userId,String groupId,List<Discuss> discuss,Auth auth) {
		this.title = title;
		this.body = body;
		this.time = time;
		this.userId = userId;
		this.groupId = groupId;
		this.discuss = discuss;
		this.auth = auth;
	}
}
