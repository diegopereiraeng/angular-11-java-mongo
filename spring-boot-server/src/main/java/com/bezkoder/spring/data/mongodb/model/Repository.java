package com.bezkoder.spring.data.mongodb.model;

import com.bezkoder.spring.data.mongodb.model.providers.Owner;
import com.bezkoder.spring.data.mongodb.model.providers.Permissions;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "repositories")
public class Repository {
  @Id
  private String id;

  private String name;
  private String description;
  private boolean enabled;
  private String branch;
  private String language;
  private String owner;
  private String[] contributors;
  private String provider;
  private String link;
  private String visibility;
  private String full_name;
  private Boolean fork;
  private Permissions permissions;
  private String group;

  public Repository() {

  }

  public Repository(String name, String description, boolean enabled, String branch, String language, String owner, String provider) {
    this.name = name;
    this.description = description;
    this.enabled = enabled;
    this.branch = branch;
    this.language = language;
    this.owner = owner;
    this.provider = provider;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean isEnabled) {
    this.enabled = isEnabled;
  }

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String[] getContributors() {
    return contributors;
  }

  public void setContributors(String[] contributors) {
    this.contributors = contributors;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  @Override
  public String toString() {
    return "Repository [id=" + id + ", name=" + name + ", desc=" + description + ", enabled=" + enabled + ", branch=" + branch + ", language=" + language + ", owner=" + owner + ", contributors=" + contributors + "]";
  }

}