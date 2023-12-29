package com.lucas.multidb.controller;

import com.lucas.multidb.mongo.model.Report;
import com.lucas.multidb.oracle.model.UserLegacy;
import com.lucas.multidb.oracle.repository.UserLegacyRepository;
import com.lucas.multidb.postgres.model.User;
import com.lucas.multidb.postgres.repository.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("transfer")
public class Transfer {

    private final MongoTemplate mongoTemplate;
    private final UserLegacyRepository userLegacyRepository;
    private final UserRepository userRepository;

    public Transfer(MongoTemplate mongoTemplate, UserLegacyRepository userLegacyRepository, UserRepository userRepository) {
        this.mongoTemplate = mongoTemplate;
        this.userLegacyRepository = userLegacyRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/test")
    void save() {
        try {

            List<UserLegacy> legacyList = userLegacyRepository.findAll();

            List<User> newUsers = legacyList.stream().map(userLegacy -> {
                return User.builder()
                        .cpf(userLegacy.getCpf())
                        .id(userLegacy.getId())
                        .email(userLegacy.getEmail())
                        .nome(userLegacy.getNome())
                        .build();
            }).collect(Collectors.toList());

            userRepository.saveAll(newUsers);

            var report = new Report();
            report.setStatus("OK");
            report.setCreateAt(LocalDateTime.now());
            mongoTemplate.save(report, "Report");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

