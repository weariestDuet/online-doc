package com.example.controller;

import com.example.dao.TemplateDao;
import com.example.entity.Result;
import com.example.entity.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    TemplateDao templateDao;


    @GetMapping("/getAll")
    public Result getAll(){
        try {
            return Result.create(200, "获取成功",templateDao.getTemplate());
        }
        catch (Exception e) {
            return Result.create(200, "获取失败" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        try {
            Template template = templateDao.getTemplateById(id);
            return Result.create(200, "获取成功",template);
        }
        catch (Exception e) {
            return Result.create(200, "获取失败" + e.getMessage());
        }
    }
}
