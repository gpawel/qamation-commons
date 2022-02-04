package org.qamation.commons.config;

import org.openqa.selenium.WebDriver;
import org.qamation.commons.utils.ResourceUtils;

import java.util.Properties;

public  class Config {

    private static Config config = null;

    public static Config getConfigProperties() {
        if (config  == null) {
            config = new Config();
        }
        return config;
    }

    private String rootPath;
    private String resourcesPath;
    private String env;

    private Config() {
        env = System.getProperty("env");
        if (env == null) {
            System.err.println("env is not provided. Please set -Denv=<folder> what is /resources/env/<folder> ");
            System.exit(-1);
        }

        rootPath = System.getProperty("ROOT");

        if (rootPath == null) rootPath = System.getProperty("user.dir");
        resourcesPath = rootPath+ "/resources";

        String defaultProps = resourcesPath + "/etc";
        String envProps = resourcesPath + "/env/"+env;
        loadProperties(defaultProps);
        loadProperties(envProps);
    }



    public String getRootPath() {
        return rootPath;
    }

    public String getResourcesPath() {
        return resourcesPath;
    }

    public String getEnv() {
        return env;
    }

    public Properties getLoadedProperties() {
        return System.getProperties();
    }

    private void loadProperties(String prop_path) {
        if (prop_path.isEmpty()) {
            ResourceUtils.loadProperties();
        } else {
            ResourceUtils.loadProperties(prop_path);
        }
    }

}

