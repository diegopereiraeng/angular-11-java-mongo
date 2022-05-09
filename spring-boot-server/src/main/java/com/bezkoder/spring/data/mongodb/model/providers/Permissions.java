package com.bezkoder.spring.data.mongodb.model.providers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Permissions {
    private Boolean admin;
    private Boolean maintain;
    private Boolean push;
    private Boolean triage;
    private Boolean pull;

    public Permissions() {
    }

    public Permissions(Boolean admin, Boolean maintain, Boolean push, Boolean triage, Boolean pull) {
        this.admin = admin;
        this.maintain = maintain;
        this.push = push;
        this.triage = triage;
        this.pull = pull;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getMaintain() {
        return maintain;
    }

    public void setMaintain(Boolean maintain) {
        this.maintain = maintain;
    }

    public Boolean getPush() {
        return push;
    }

    public void setPush(Boolean push) {
        this.push = push;
    }

    public Boolean getTriage() {
        return triage;
    }

    public void setTriage(Boolean triage) {
        this.triage = triage;
    }

    public Boolean getPull() {
        return pull;
    }

    public void setPull(Boolean pull) {
        this.pull = pull;
    }

    @Override
    public String toString() {
        return "Permissions{" +
                "admin=" + admin +
                ", maintain=" + maintain +
                ", push=" + push +
                ", triage=" + triage +
                ", pull=" + pull +
                '}';
    }
}
