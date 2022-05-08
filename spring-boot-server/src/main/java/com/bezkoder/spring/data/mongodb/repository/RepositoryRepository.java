package com.bezkoder.spring.data.mongodb.repository;

import com.bezkoder.spring.data.mongodb.model.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RepositoryRepository extends MongoRepository<Repository, String> {
  List<Repository> findByEnabled(boolean enabled);
  List<Repository> findByNameContaining(String name);
}
