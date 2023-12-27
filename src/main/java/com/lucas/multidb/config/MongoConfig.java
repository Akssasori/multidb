//package com.lucas.multidb.config;
//
//import com.mongodb.client.MongoClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
//
//@Configuration
//public class MongoConfig {
//
//    @Value("${spring.data.mongodb.database}")
//    private String mongoDatabase;
//
//    public MongoTemplate mongoTemplate() throws Exception {
//        return new MongoTemplate(mongoDbFactory());
//    }
//
//    private MongoDatabaseFactory mongoDbFactory() throws Exception {
//       return new SimpleMongoClientDatabaseFactory(new MongoClient(uri),mongoDatabase);
//    }
//}
