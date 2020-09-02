package com.example.controller;

import com.example.dao.DocDao;
import com.example.dao.UserDao;
import com.example.entity.Doc;
import com.example.entity.User;
import com.example.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Discuss;
import com.example.entity.Result;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/discuss")
public class DiscussController {
	@Autowired
	UserDao userDao;
	@Autowired
	DocDao docDao;
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	private HttpServletRequest request;

	@PostMapping("/send/{docId}")
	Result newDiscuss(@PathVariable String docId, String discussBody) {
		try {
			User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
			docDao.saveDiscuss(user,docId,discussBody);
			return Result.create(200, "评论成功");
		}
		catch (Exception e) {
			return Result.create(200, "评论失败" + e.getMessage());
		}
	}

	@DeleteMapping("/delete/{discussId}")
	Result deleteDiscuss(@PathVariable Integer discussId) {
		try {
//			docDao.deleteDiscuss(discussId);
			return Result.create(200, "删除成功");
		}
		catch (Exception e) {
			return Result.create(200, "删除失败" + e.getMessage());
		}
	}

	@PostMapping("/read/{discussId}")
	Result readDiscuss(@PathVariable Integer discussId) {
		try {
			return Result.create(200, "已读成功");
		}
		catch (Exception e) {
			return Result.create(200, "已读失败" + e.getMessage());
		}
	}

	@GetMapping("/getByDocId/{fileId}")
	Result getDiscussByDocId(@PathVariable String fileId) {
		return Result.create(200, "查询成功", null);
	}

	@GetMapping("/getById")
	Result getDiscussById(Integer discussId) {
		try {
			return Result.create(200, "查询成功", null);
		}
		catch (Exception e) {
			return Result.create(200, "查询失败," + e.getMessage());
		}
	}

	@GetMapping("/getAllDiscuss")
	Result getAllDiscuss(){
		try {
			return Result.create(200, "查询成功", null);
		}
		catch (Exception e) {
			return Result.create(200, "查询失败," + e.getMessage());
		}
	}
}
