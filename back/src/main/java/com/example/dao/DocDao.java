package com.example.dao;

import com.example.entity.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

@Component
public class DocDao {
    private final Random random = new Random();
    @Resource
    private MongoTemplate mongoTemplate;

    public void saveDoc(Doc doc) {
        mongoTemplate.save(doc);
    }
    public void update(Doc doc) {
        Query query = new Query(Criteria.where("docId").is(doc.getDocId()));
        Update update = new Update();
        update.set("title", doc.getTitle());
        update.set("body", doc.getBody());
        update.set("time", doc.getTime());
        mongoTemplate.updateFirst(query, update, Doc.class);
    }
    public Doc getDoc(String title) {
        Query query = new Query(Criteria.where("title").is(title));
        return mongoTemplate.findOne(query,Doc.class);
    }

    public Doc getDocById(String docId) {
        Query query = new Query(Criteria.where("_id").is(docId));
        return mongoTemplate.findOne(query,Doc.class);
    }

    public void collect(String userId, String docId, String title) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.push("favor",new Favor(docId,title));
        mongoTemplate.updateFirst(query, update, User.class);
    }
    public void unCollect(String userId, Favor favor) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.pull("favor",favor);
        mongoTemplate.updateFirst(query, update, User.class);
    }
    public void CreationU(String userId, String docId, String title) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.push("creation",new Creation(docId,title));
        mongoTemplate.updateFirst(query, update, User.class);
    }
    public void CreationT(String groupId, String docId, String title) {
        Query query = new Query(Criteria.where("groupId").is(groupId));
        Update update = new Update();
        update.push("creation",new Creation(docId,title));
        mongoTemplate.updateFirst(query, update, Group.class);
    }
    public void unCreationU(String userId, Creation creation) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.pull("creation",creation);
        mongoTemplate.updateFirst(query, update, User.class);
    }
    public void unCreationT(String groupId, Creation creation) {
        Query query = new Query(Criteria.where("groupId").is(groupId));
        Update update = new Update();
        update.pull("creation",creation);
        mongoTemplate.updateFirst(query, update, Group.class);
    }


    public void delete(String docId) {
        Query query = new Query(Criteria.where("docId").is(docId));
        mongoTemplate.remove(query,Doc.class);
    }

    public void saveDiscuss(User user, String docId, String discussBody) {
        Query query = new Query(Criteria.where("docId").is(docId));
        Update update = new Update();
        Integer discussId = random.nextInt(899999) + 100001;
        update.push("discuss",new Discuss(discussId,discussBody,new Date(),user.getName(),user.getAvatar(),0));
        mongoTemplate.updateFirst(query, update, Doc.class);
    }


}
