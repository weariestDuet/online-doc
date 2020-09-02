package com.example.dao;

import com.example.entity.Creation;
import com.example.entity.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public void saveUser(User user){
        mongoTemplate.save(user);
    }

    public User getUserByName(String name){
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query,User.class);
    }

    public void saveAvatar(String userId, String avatar){
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.set("avatar", avatar);
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public void updateUser(String name, String info) {
        Query query = new Query(Criteria.where("name").is(name));
        Update update = new Update();
        update.set("info", info);
        mongoTemplate.updateFirst(query, update, User.class);
    }

}
