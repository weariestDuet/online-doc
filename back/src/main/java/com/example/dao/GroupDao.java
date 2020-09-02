package com.example.dao;

import com.alibaba.fastjson.JSON;
import com.example.entity.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class GroupDao {
    @Resource
    private MongoTemplate mongoTemplate;


    public void create(User user, String groupName) {
        Member admin = new Member(user.getName(),user.getAvatar(),user.getMail());
        Group group = new Group(groupName, admin, null);
        mongoTemplate.save(group);
    }

    public Group getGroup(String groupName) {
        Query query = new Query(Criteria.where("name").is(groupName));
        return mongoTemplate.findOne(query, Group.class);
    }

    public List<Group> getUserGroup(String name) {
        Query query1 = new Query(Criteria.where("admin.name").is(name));
        List<Group> admin = mongoTemplate.find(query1, Group.class);
        Query query2 = new Query(Criteria.where("member.name").is(name));
        List<Group> lists = mongoTemplate.find(query2, Group.class);
        lists.addAll(admin);
        return lists;
    }



    public void removeMem(String name, Member member) {
        Query query = new Query(Criteria.where("name").is(name));
        Update update = new Update();
        update.pull("member",member);
        mongoTemplate.updateFirst(query, update, Group.class);
    }

    public void deleteTeam(String teamName) {
        Query query = new Query(Criteria.where("name").is(teamName));
        mongoTemplate.remove(query, Group.class);
    }

    public void addMem(User user, String name) {
        Member member = new Member(user.getName(),user.getAvatar(),user.getMail());
        Query query = new Query(Criteria.where("name").is(name));
        Update update = new Update();
        update.push("member",member);
        mongoTemplate.updateFirst(query, update, Group.class);
    }
}
