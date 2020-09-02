package com.example;

import com.alibaba.fastjson.JSONObject;
import com.example.dao.TemplateDao;
import com.example.entity.Template;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class demo {

    @Autowired
    UserService userService;
    @Autowired
    TemplateDao templateDao;

    @Test
    void save() {
//        templateDao.saveTemplate();
    }
    @Test
    void find() {
        System.out.println(JSONObject.toJSONString(templateDao.getTemplate()));
    }
    @Test
    void findSize()
    {
        List<Template> all = templateDao.getTemplate();
        System.out.println(all.size());
    }


}
