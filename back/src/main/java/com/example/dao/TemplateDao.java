package com.example.dao;

import com.example.entity.Template;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TemplateDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public List<Template> getTemplate() {
        return mongoTemplate.findAll(Template.class);
    }

    public Template getTemplateById(Integer id) {
        Query query = new Query(Criteria.where("templateId").is(id));
        return mongoTemplate.findOne(query,Template.class);
    }
}
