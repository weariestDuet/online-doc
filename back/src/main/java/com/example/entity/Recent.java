package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Recent {
	private String recentId;
	private String docId;
	private String title;

	public Recent(String docId, String title) {
		this.docId = docId;
		this.title = title;
	}
}
