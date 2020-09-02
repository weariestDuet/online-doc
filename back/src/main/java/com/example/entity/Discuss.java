package com.example.entity;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Discuss {
	private Integer discussId;
	private String content;
	private Date time;
	private String name;
	private String avatar;
	private Integer isRead;

	public Discuss(Integer discussId, String content, Date time, String name, String avatar, Integer isRead) {
		this.discussId = discussId;
		this.content = content;
		this.time = time;
		this.name = name;
		this.avatar = avatar;
		this.isRead = isRead;
	}
}
