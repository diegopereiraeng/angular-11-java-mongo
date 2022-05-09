package com.bezkoder.spring.data.mongodb.model.providers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner {
    private Long id;
    private String login;
    private String type;
    private Boolean site_admin;

    public Owner() {

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Owner(Long id, String login, String type, Boolean site_admin) {
        this.id = id;
        this.login = login;
        this.type = type;
        this.site_admin = site_admin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSite_admin() {
        return site_admin;
    }

    public void setSite_admin(Boolean site_admin) {
        this.site_admin = site_admin;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", type='" + type + '\'' +
                ", site_admin=" + site_admin +
                '}';
    }
}
