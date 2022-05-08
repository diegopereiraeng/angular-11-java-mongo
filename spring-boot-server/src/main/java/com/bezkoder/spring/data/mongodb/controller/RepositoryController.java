package com.bezkoder.spring.data.mongodb.controller;

import com.bezkoder.spring.data.mongodb.SpringBootDataMongodbApplication;
import com.bezkoder.spring.data.mongodb.model.Repository;
import com.bezkoder.spring.data.mongodb.model.providers.Github;
import com.bezkoder.spring.data.mongodb.repository.RepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//@CrossOrigin(origins = "http://angular.harness-demo.site")
//@CrossOrigin(origins = {"http://34.122.165.247"})
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RepositoryController {

  @Autowired
  RepositoryRepository repositoryRepository;

  @Autowired
  private RestTemplate restTemplate;

  private static final Logger log = LoggerFactory.getLogger(SpringBootDataMongodbApplication.class);

  private static String url = "http://localhost:8080/api/quote";



  @GetMapping("/quote")
  public String showQuote() {
    String quote = "{ \"type\": \"success\", \"value\": { \"id\": 10, \"quote\": \"Really loving Spring Boot, makes stand alone Spring apps easy.\" }}";

    return quote;
  }

  @GetMapping("/repositories")
  public ResponseEntity<List<Repository>> getAllRepositories(@RequestParam(required = false) String Name) {
    try {
      Object quote = restTemplate.getForObject(url,Github.class);
      log.info(quote.toString());
      //System.out.println("called");
      List<Repository> repositories = new ArrayList<Repository>();

      if (Name == null)
        repositoryRepository.findAll().forEach(repositories::add);
      else
        repositoryRepository.findByNameContaining(Name).forEach(repositories::add);

      if (repositories.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(repositories, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/repositories/{id}")
  public ResponseEntity<Repository> getRepositoryById(@PathVariable("id") String id) {
    Optional<Repository> repositoryData = repositoryRepository.findById(id);

    if (repositoryData.isPresent()) {
      return new ResponseEntity<>(repositoryData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/repositories/discover")
  public ResponseEntity<List<Repository>> discoverAllRepositories(@RequestParam(required = false) String Name) {
    try {
      System.out.println("called");
      List<Repository> repositories = new ArrayList<Repository>();

      if (Name == null)
        repositoryRepository.findAll().forEach(repositories::add);
      else
        repositoryRepository.findByNameContaining(Name).forEach(repositories::add);

      if (repositories.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(repositories, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/repositories")
  public ResponseEntity<Repository> createRepository(@RequestBody Repository repository) {
    try {
      Repository _repository = repositoryRepository.save(new Repository(repository.getName(), repository.getDescription() , false, repository.getBranch(), repository.getLanguage(), repository.getOwner(), repository.getContributors(), repository.getProvider() ));
      return new ResponseEntity<>(_repository, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/repositories/{id}")
  public ResponseEntity<Repository> updateRepository(@PathVariable("id") String id, @RequestBody Repository repository) {
    Optional<Repository> repositoryData = repositoryRepository.findById(id);

    if (repositoryData.isPresent()) {
      Repository _repository = repositoryData.get();
      _repository.setName(repository.getName());
      _repository.setDescription(repository.getDescription());
      _repository.setEnabled(repository.isEnabled());
      return new ResponseEntity<>(repositoryRepository.save(_repository), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/repositories/{id}")
  public ResponseEntity<HttpStatus> deleteRepository(@PathVariable("id") String id) {
    try {
      repositoryRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/repositories")
  public ResponseEntity<HttpStatus> deleteAllRepositories() {
    try {
      repositoryRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/repositories/enabled")
  public ResponseEntity<List<Repository>> findByEnabled() {
    try {
      List<Repository> repositories = repositoryRepository.findByEnabled(true);

      if (repositories.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(repositories, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
