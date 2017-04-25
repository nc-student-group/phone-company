package com.phonecompany.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.phonecompany.exception.DataSourceConfigurationFileParseException;
import com.phonecompany.model.config.Config;
import com.phonecompany.model.config.DataSourceInfo;
import com.phonecompany.model.config.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Loads configuration properties from the configuration source.
 *
 * Allows architecture to be flexible and source code not be recompiled every
 * time configuration properties change their values.
 */
public class ConfigManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);
    private static final String APPLICATION_CONFIG = "datasource-config.yml";

    private static ConfigManager configManager;
    private DataSourceInfo dataSourceInfo;

    private ConfigManager() {
        this.loadDataSourceInfo();
    }

    /**
     * Returns a single instance of the config provider
     * that this class {@code ConfigManager} represents
     *
     * @return single {@code ConfigManager} object
     */
    public static ConfigManager getInstance() {
        if(configManager == null) {
            configManager = new ConfigManager();
        }
        return configManager;
    }

    /**
     * Responsible for the fact, that property file is being
     * loaded only once
     *
     * @return fully constructed configuration object
     * @see    DataSourceInfo configuration class
     */
    public DataSourceInfo getDataSourceInfo() {
        if(this.dataSourceInfo == null) {
            this.loadDataSourceInfo();
        }
        return dataSourceInfo;
    }

    /**
     * Maps properties from the configuration file to the corresponding
     * configuration object
     *
     * @throws DataSourceConfigurationFileParseException if mapping has
     *         failed or no configuration file has been found
     * @see    DataSourceInfo configuration class
     */
    private void loadDataSourceInfo() throws DataSourceConfigurationFileParseException {
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
            throw new DataSourceConfigurationFileParseException(APPLICATION_CONFIG, e);
        }
    }

    /**
     * Iterates over the list of the profiles defined in a property file
     * and returns the only one that matches an active one
     *
     * @param config object representing the whole configuration file
     * @return       profile that matches an active one or {@literal null}
     *               if none was found
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
