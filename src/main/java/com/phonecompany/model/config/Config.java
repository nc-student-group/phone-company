package com.phonecompany.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * <p>Class used to represent a configuration file as a java object.
 * Corresponding configuration file should follow certain structure
 * in order to fit in this configuration class. For example:
 * </p>
 * <pre class="code">
 *  active: active_profile_name
 *  profiles:
 *      - name: prod
 *          datasource:
 *          url: datasource_url
 *          driver-class-name: driver_name
 *          username: your_username
 *          password: your_pass
 * </pre>
 */
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
