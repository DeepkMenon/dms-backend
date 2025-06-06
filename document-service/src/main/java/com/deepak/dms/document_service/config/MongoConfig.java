package com.deepak.dms.document_service.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

  @Value("${spring.data.mongodb.database}")
  private String databaseName;

  @Bean
  public GridFSBucket gridFsBucket(MongoClient mongoClient) {
    MongoDatabase database = mongoClient.getDatabase(databaseName);
    return GridFSBuckets.create(database);
  }
}
