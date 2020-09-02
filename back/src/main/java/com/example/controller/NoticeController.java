package com.example.controller;

import com.example.dao.GroupDao;
import com.example.dao.NoticeDao;
import com.example.dao.UserDao;
import com.example.entity.Group;
import com.example.entity.Notice;
import com.example.entity.Result;
import com.example.entity.User;
import com.example.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    UserDao userDao;
    @Autowired
    NoticeDao noticeDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public Result getUserNotice(){
        try {
            User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
            return Result.create(200, "获取成功", noticeDao.getNotice(user.getName()));
        }
        catch(Exception e) {
            return Result.create(200, "获取失败," + e.getMessage());
        }
    }
    @PostMapping("/read/{noticeId}")
    public Result ignore(@PathVariable String noticeId){
        try {
            noticeDao.readNotice(noticeId);
            return Result.create(200, "已读成功");
        }
        catch(Exception e) {
            return Result.create(200, "已读失败," + e.getMessage());
        }
    }
    @PostMapping("/permit/{noticeId}")
    public Result permit(@PathVariable String noticeId){
        try {
            Notice notice = noticeDao.getNoticeById(noticeId);
            Group group = groupDao.getGroup(notice.getItem());
            User user = userDao.getUserByName(notice.getSender());
            groupDao.addMem(user,group.getName());
            noticeDao.readNotice(noticeId);
            return Result.create(200, "批准成功");
        }
        catch(Exception e) {
            return Result.create(200, "批准失败," + e.getMessage());
        }
    }
}
