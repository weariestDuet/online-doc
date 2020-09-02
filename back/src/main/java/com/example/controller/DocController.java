package com.example.controller;

import com.example.dao.DocDao;
import com.example.dao.GroupDao;
import com.example.dao.UserDao;
import com.example.entity.Result;
import com.example.entity.*;

import java.util.Date;
import java.util.List;

import com.example.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@CrossOrigin
@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    UserDao userDao;
    @Autowired
    DocDao docDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/{docId}")
    public Result findDocById(@PathVariable String docId) {
    	try {
    		return Result.create(200, "查询成功", docDao.getDocById(docId));
    	}catch(Exception e) {
    		return Result.create(200, "查询失败," + e.getMessage());
    	}
    }
    @PostMapping("/saveDoc")
    public Result newDoc(String title, String body) {
    	try {
            User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
            Doc doc = new Doc(title,body,new Date(),user.getUserId(),"0",null,new Auth());
            docDao.saveDoc(doc);
            docDao.CreationU(user.getUserId(),docDao.getDoc(title).getDocId(),title);
            return Result.create(200, "上传成功");
    	}
    	catch(Exception e) {
    		return Result.create(200, "上传失败," + e.getMessage());
    	}
    }
    @PostMapping("/saveTeamDoc")
    public Result newTeamDoc(String title, String body ,String teamName) {
        try {
            Group group = groupDao.getGroup(teamName);
            Doc doc = new Doc(title,body,new Date(),"0",group.getGroupId(),null,new Auth());
            docDao.saveDoc(doc);
            docDao.CreationT(group.getGroupId(),docDao.getDoc(title).getDocId(),title);
            return Result.create(200, "上传成功");
        }
        catch(Exception e) {
            return Result.create(200, "上传失败," + e.getMessage());
        }
    }
    @PostMapping("/edit")
    public Result newDoc(Integer docId, String docTitle, String docBody) {
    	try {
    		return Result.create(200, "编辑成功");
    	}
    	catch(Exception e) {
    		return Result.create(200, "编辑失败," + e.getMessage());
    	}
    }
    @PostMapping("/update")
    public Result updateDoc(String docId, String title, String body) {
        try {
            Doc doc = docDao.getDocById(docId);
            doc.setTitle(title);
            doc.setBody(body);
            doc.setTime(new Date());
            docDao.update(doc);
            return Result.create(200, "更新成功");
        } catch (RuntimeException e) {
            return Result.create(200, "更新失败，" + e.getMessage());
        }
    }
    @PostMapping("/{docId}/updateAuth")
    public Result updateAuth(@PathVariable Integer docId, String role, String auth){
        try {
            return Result.create(200, "修改成功");
        } catch (RuntimeException e) {
            return Result.create(200, "修改失败" + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{docId}")
    Result delete(@PathVariable String docId) {
        try {
            Doc doc = docDao.getDocById(docId);
            Creation creation = new Creation(doc.getDocId(),doc.getTitle());
            Favor favor = new Favor(doc.getDocId(),doc.getTitle());
            docDao.unCreationU(doc.getUserId(),creation);
            docDao.unCollect(doc.getUserId(),favor);
            docDao.unCreationT(doc.getGroupId(),creation);
            docDao.delete(docId);
            return Result.create(200, "删除成功");
        } catch (RuntimeException e) {
            return Result.create(200, "删除失败" + e.getMessage());
        }
    }
    @PostMapping("/collect/{docId}")
    Result collect(@PathVariable String docId) {
        try {
            User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
            Doc doc = docDao.getDocById(docId);
            docDao.collect(user.getUserId(),docId,doc.getTitle());
            return Result.create(200, "收藏成功");
        }
        catch(Exception e) {
            return Result.create(200, "收藏失败," + e.getMessage());
        }
    }
    @DeleteMapping("/unCollect/{docId}")
    Result unCollect(@PathVariable String docId) {
        User user = userDao.getUserByName(jwtTokenUtil.getUsernameFromRequest(request));
        Doc doc = docDao.getDocById(docId);
        docDao.unCollect(user.getUserId(),new Favor(docId,doc.getTitle()));
        return Result.create(200, "删除成功");
    }


}
