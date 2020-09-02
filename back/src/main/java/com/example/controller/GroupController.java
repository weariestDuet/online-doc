package com.example.controller;

import com.example.dao.DocDao;
import com.example.dao.GroupDao;
import com.example.dao.NoticeDao;
import com.example.dao.UserDao;
import com.example.entity.*;
import com.example.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/team")
public class GroupController {
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

    @PostMapping("/create/{groupName}")
    public Result createGroup(@PathVariable String groupName){
        try {
            User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
            groupDao.create(user, groupName);
            return Result.create(200, "创建成功");
        }
        catch(Exception e) {
            return Result.create(201, "创建失败," + e.getMessage());
        }
    }
    @GetMapping("/getGroups")
    public Result getGroups(){
        try {
            User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
            return Result.create(200, "获取成功",groupDao.getUserGroup(user.getName()));
        }
        catch(Exception e) {
            return Result.create(200, "获取失败," + e.getMessage());
        }
    }
    @PostMapping("/addMem")
    public Result saveGroupMem(String userName,String teamName){
        try {
            Group group = groupDao.getGroup(teamName);
            Notice notice = new Notice(userName,group.getAdmin().getName(),"邀请你加入",group.getName(),new Date(),0);
            noticeDao.saveNotice(notice);
            return Result.create(200, "邀请发送成功");
        }
        catch(Exception e) {
            return Result.create(201, "邀请发送失败," + e.getMessage());
        }
    }
    @PostMapping("/attend/{groupName}")
    public Result attendGroup(@PathVariable String groupName){
        try {
            User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
            Group group = groupDao.getGroup(groupName);
            Notice notice = new Notice(group.getAdmin().getName(),user.getName(),"申请加入",group.getName(),new Date(),0);
            noticeDao.saveNotice(notice);
            return Result.create(200, "申请发送成功");
        }
        catch(Exception e) {
            return Result.create(201, "申请发送失败," + e.getMessage());
        }
    }
    @PostMapping("/removeMem")
    public Result removeMem(String teamName,String userName){
        try {
            User user = userDao.getUserByName(userName);
            Member member = new Member(userName,user.getAvatar(),user.getMail());
            groupDao.removeMem(teamName,member);
            return Result.create(200, "移除成员成功");
        }
        catch(Exception e) {
            return Result.create(201, "移除成员失败," + e.getMessage());
        }
    }
    @PostMapping("/deleteGroup")
    public Result deleteGroup(String teamName){
        try {
            groupDao.deleteTeam(teamName);
            return Result.create(200, "解散团队成功");
        }
        catch(Exception e) {
            return Result.create(201, "解散团队失败," + e.getMessage());
        }
    }
}
