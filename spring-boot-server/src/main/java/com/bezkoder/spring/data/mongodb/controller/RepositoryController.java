package com.bezkoder.spring.data.mongodb.controller;

import com.bezkoder.spring.data.mongodb.SpringBootDataMongodbApplication;
import com.bezkoder.spring.data.mongodb.authservice.model.InstaUserDetails;
import com.bezkoder.spring.data.mongodb.authservice.payload.UserSummary;
import com.bezkoder.spring.data.mongodb.model.Repository;
import com.bezkoder.spring.data.mongodb.model.providers.Github;
import com.bezkoder.spring.data.mongodb.model.providers.Quote;
import com.bezkoder.spring.data.mongodb.repository.RepositoryRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.harness.cf.client.api.Event;
import io.harness.cf.client.dto.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.bezkoder.spring.data.mongodb.model.FeatureFlagsService;


//@CrossOrigin(origins = "http://angular.harness-demo.site")
//@CrossOrigin(origins = {"http://34.122.165.247"})

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RepositoryController {

  @Autowired
  RepositoryRepository repositoryRepository;

  @Autowired
  private RestTemplate restTemplate;

  Dotenv dotenv = Dotenv.load();

  private static final Logger log = LoggerFactory.getLogger(SpringBootDataMongodbApplication.class);

  private final FeatureFlagsService ffClient;



  public RepositoryController(){
    System.out.println("SDK Key: "+dotenv.get("FF_SDK_KEY"));
    //System.out.println("SDK Key variable: "+this.sdkKey);
    this.ffClient =  new FeatureFlagsService(dotenv.get("FF_SDK_KEY"),"Repository");
  }


  //private String url = "http://localhost:8080/api/quote";
  private String url = dotenv.get("QUOTE_URL");

  private String github = dotenv.get("GITHUB_URL");


  @Value("${github.credentials}")
  String githubAuth;

  private void ConfigApp(String configs, String type) {
    System.out.println("Config App");
    log.info("Flag {} {}", type, configs);

  }

  private String ConfigApp(String flag) {
    System.out.println("Ready App");
    log.info("Ready Flag {}",  flag);
    return flag;
  }

  @RequestMapping("/")
  public String home() {
    return "{ \"id\": \"abc1-34ef-56jh-78il\", \"content\": \"Hello Customer\" }";
  }

  @GetMapping("/quote")
  public String showQuote() {
    String quote = "{ \"type\": \"success\", \"value\": { \"id\": 10, \"quote\": \"Really loving Spring Boot, makes stand alone Spring apps easy.\" }}";
    Target target = Target.builder().name("Java_Backend").identifier("javabackend@harness.io").build();
    String menuVersion = ffClient.featureFlagService.stringVariation("Menu_Version", target, "v1");
    ffClient.featureFlagService.on(Event.READY, resultReady -> log.info("Flag ready {}",ConfigApp(resultReady)));
    ffClient.featureFlagService.on(Event.CHANGED, resultChanged -> ConfigApp(resultChanged, "changed"));
    //ffClient.featureFlagService.on(Event.CHANGED, resultChanged -> log.info("Flag changed {}", resultChanged));
    System.out.println("Flag Value: "+menuVersion);


    return quote;
  }

  @PreAuthorize("hasRole('USER') or hasRole('FACEBOOK_USER')")
  @GetMapping("/repositories")
  public ResponseEntity<List<Repository>> getAllRepositories(@RequestParam(required = false) String Name, @AuthenticationPrincipal InstaUserDetails userDetails) {
    UserSummary user = UserSummary
            .builder()
            .id(userDetails.getId())
            .username(userDetails.getUsername())
            .name(userDetails.getUserProfile().getDisplayName())
            .profilePicture(userDetails.getUserProfile().getProfilePictureUrl())
            .email(userDetails.getEmail())
            .type(userDetails.getRoles().stream().findFirst().get().getName())
            .build();

    Target target = Target.builder().name(user.getName()).identifier(user.getUsername()).attribute("email",user.getEmail()).attribute("userType",user.getType()).build();
    Boolean ffDecision = this.ffClient.boolCheck("Repository_Filter",target, false);
    if (ffDecision){
      System.out.println("Repository filter is on");
      try {
//        Object quote = restTemplate.getForObject(url,Quote.class);
//        log.info(quote.toString());
        System.out.println("target user: " + user.getUsername());
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
        System.out.println("Error Message: "+e.getMessage());
        System.out.println("Error Cause: "+e.getCause());
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    else {
      try {
        Object quote = restTemplate.getForObject(url,Quote.class);
        log.info(quote.toString());
        //System.out.println("called");
        List<Repository> repositories = new ArrayList<Repository>();

        repositoryRepository.findAll().forEach(repositories::add);

        if (repositories.isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(repositories, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }

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
    System.out.println("Discovering Repositories in Github");
    try {
      List<Repository> repositories = new ArrayList<Repository>();

      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", githubAuth);
      headers.set("Accept","application/vnd.github.v3+json");
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

      ResponseEntity<Github[]> response = restTemplate.exchange(
              github, HttpMethod.GET, requestEntity, Github[].class);

      System.out.println("Parsing Results...");

      System.out.println("Response: "+response.getStatusCode().toString());

      Github[] githubRepos = response.getBody();


      List<Github> repoList = Arrays.asList(githubRepos);

      repoList.forEach((Github repo) -> {
        System.out.println(repo.getName());
        try {
          Repository _repository = repositoryRepository.save(new Repository(repo.getName(), repo.getDescription() , false, repo.getDefault_branch(), repo.getLanguage(), repo.getOwner().getLogin(), repo.getName() ));

        } catch (Exception e) {
          log.error("Error saving repo: "+repo.getName());
        }

      });


      log.info(response.getStatusCode().toString());
      //Object github_object = restTemplate.getForObject(github, Github.class);
      log.info(response.getBody().toString());

      if (Name == null)
        repositoryRepository.findAll().forEach(repositories::add);
      else
        repositoryRepository.findByNameContaining(Name).forEach(repositories::add);

      if (repositories.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(repositories, HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/repositories")
  public ResponseEntity<Repository> createRepository(@RequestBody Repository repository) {
    try {
      Repository _repository = repositoryRepository.save(new Repository(repository.getName(), repository.getDescription() , false, repository.getBranch(), repository.getLanguage(), repository.getOwner(), repository.getProvider() ));
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
