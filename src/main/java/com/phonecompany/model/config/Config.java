package com.phonecompany.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Config {

    @JsonProperty("active")
    private String activeProfileName;
    private List<Profile> profiles;

    public Config() {
    }

    public Config(String active, List<Profile> profiles) {
        this.activeProfileName = active;
        this.profiles = profiles;
    }

    public String getActiveProfileName() {
        return activeProfileName;
    }

    public void setActiveProfileName(String activeProfileName) {
        this.activeProfileName = activeProfileName;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }
}
