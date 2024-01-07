package com.lucas.multidb.controller;

import com.lucas.multidb.mongo.model.Report;
import com.lucas.multidb.oracle.model.UserLegacy;
import com.lucas.multidb.oracle.repository.UserLegacyRepository;
import com.lucas.multidb.postgres.model.User;
import com.lucas.multidb.postgres.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("dataTransfer")
@Log4j2
public class TransferDataController {

    private final MongoTemplate mongoTemplate;
    private final UserLegacyRepository userLegacyRepository;
    private final UserRepository userRepository;

    public TransferDataController(MongoTemplate mongoTemplate, UserLegacyRepository userLegacyRepository, UserRepository userRepository) {
        this.mongoTemplate = mongoTemplate;
        this.userLegacyRepository = userLegacyRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/start")
    public ResponseEntity<String> save() {

        try {

            List<UserLegacy> legacyList = userLegacyRepository.findAll();

            List<User> newUsers = new ArrayList<>();
            int count = 0;

            for (UserLegacy userLegacy : legacyList) {
                User build = User.builder()
                        .cpf(userLegacy.getCpf())
                        .id(userLegacy.getId())
                        .email(userLegacy.getEmail())
                        .nome(userLegacy.getNome())
                        .build();
                newUsers.add(build);
                count++;
            }

            userRepository.saveAll(newUsers);

            saveLogInMongoDb(count);

            log.info("All users have been saved");
            return ResponseEntity.ok().body("All users have been imported");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar os dados: " + e);
        }
    }

    public void saveLogInMongoDb(int count) {

        var report = new Report();
        report.setStatus("OK");
        report.setCreateAt(LocalDateTime.now());
        report.setQtd(count);

        mongoTemplate.save(report, "Report");
    }
}

