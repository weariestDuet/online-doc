package com.example.dao;

import com.example.entity.Doc;
import com.example.entity.Notice;
import com.example.entity.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class NoticeDao {
    @Resource
    MongoTemplate mongoTemplate;

    public List<Notice> getNotice(String name) {
        Query query = new Query(Criteria.where("receiver").is(name));
        return mongoTemplate.find(query, Notice.class);
    }

    public Notice getNoticeById(String noticeId) {
        Query query = new Query(Criteria.where("noticeId").is(noticeId));
        return mongoTemplate.findOne(query, Notice.class);
    }

    public void saveNotice(Notice notice){
        mongoTemplate.save(notice);
    }

    public void readNotice(String noticeId) {
        Query query = new Query(Criteria.where("noticeId").is(noticeId));
        Update update = new Update();
        update.set("isRead", 1);
        mongoTemplate.updateFirst(query, update, Notice.class);
    }
}
