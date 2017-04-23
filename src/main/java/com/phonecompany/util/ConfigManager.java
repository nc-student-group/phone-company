package com.phonecompany.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.phonecompany.model.config.Config;
import com.phonecompany.model.config.DataSourceInfo;
import com.phonecompany.model.config.Profile;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Loads configuration properties for the project from the configuration source.
 *
 * It allows architecture to be flexible and source code not be recompiled every
 * time configuration properties change their values
 */
public class ConfigManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);
    private static final String APPLICATION_CONFIG = "datasource-config.yml";

    private static ConfigManager configManager;
    private DataSourceInfo dataSourceInfo;

    private ConfigManager() {
        this.loadDataSourceInfo();
    }

    public static ConfigManager getInstance() {
        if(configManager == null) {
            configManager = new ConfigManager();
        }
        return configManager;
    }

    public DataSourceInfo getDataSourceInfo() {
        if(this.dataSourceInfo == null) {
            this.loadDataSourceInfo();
        }
        return dataSourceInfo;
    }

    private void loadDataSourceInfo() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            InputStream resourceAsStream = this.getClass()
                    .getClassLoader().getResourceAsStream(APPLICATION_CONFIG);
            LOG.debug("Getting configuration from file: " + APPLICATION_CONFIG);
            Config config = mapper.readValue(resourceAsStream, Config.class);
            Profile activeProfile = this.findActiveProfile(config);
            LOG.debug("Active profile was set to: {}", activeProfile.getName());
            dataSourceInfo = activeProfile.getDataSource();
            LOG.debug("The following profile settings have been read: {}", dataSourceInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Iterates over the list of the profiles defined in a property file
     * and returns the only one that matches an active one
     *
     * @param config object representing the whole configuration file
     * @return profile that matches an active one
     */
    private Profile findActiveProfile(Config config) {
        List<Profile> profiles = config.getProfiles();
        LOG.debug("All profiles from the config file: {}", profiles);
        String activeProfileName = config.getActiveProfileName();
        LOG.debug("Active profile name: {}", activeProfileName);
        return profiles.stream()
                .filter(profile -> profile.getName().equals(activeProfileName))
                .findFirst().orElse(null);
    }
}
