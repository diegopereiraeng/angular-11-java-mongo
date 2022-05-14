package com.bezkoder.spring.data.mongodb.model.providers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Github {
    private String name;
    private String full_name;
    private String visibility;
    private Owner owner;
    private String description;
    private Boolean fork;
    private String url;
    private String collaborators_url;
    private String language;
    private String default_branch;
    private Permissions permissions;

    public Github(String name, String full_name, String visibility, Owner owner, String description, Boolean fork, String url, String collaborators_url, String language, String default_branch, Permissions permissions) {
        this.name = name;
        this.full_name = full_name;
        this.visibility = visibility;
        this.owner = owner;
        this.description = description;
        this.fork = fork;
        this.url = url;
        this.collaborators_url = collaborators_url;
        this.language = language;
        this.default_branch = default_branch;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFork() {
        return fork;
    }

    public void setFork(Boolean fork) {
        this.fork = fork;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCollaborators_url() {
        return collaborators_url;
    }

    public void setCollaborators_url(String collaborators_url) {
        this.collaborators_url = collaborators_url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDefault_branch() {
        return default_branch;
    }

    public void setDefault_branch(String default_branch) {
        this.default_branch = default_branch;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }


}
