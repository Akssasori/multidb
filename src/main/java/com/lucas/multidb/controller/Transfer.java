package com.lucas.multidb.controller;

import com.lucas.multidb.mongo.model.Report;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("transfer")
public class Transfer {

    private final MongoTemplate mongoTemplate;

    public Transfer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping(value = "/test")
    void save() {
        try {
            var report = new Report();
            report.setStatus("OK");
            report.setCreateAt(LocalDateTime.now());
            mongoTemplate.save(report, "Report");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

