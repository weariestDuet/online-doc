package com.example.entity;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Favor {
	private String favorId;
	private String docId;
	private String title;

	public Favor(String docId, String title) {
		this.docId = docId;
		this.title = title;
	}
}
