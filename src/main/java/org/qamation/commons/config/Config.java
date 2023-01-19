package org.qamation.commons.config;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.qamation.commons.utils.ResourceUtils;

import java.util.Properties;

public  class Config {
    private static final String PAGE_CHANGES_NOT_STARTED_MAX_MILLS = "page.changes.not.started.max.mills";
    private static final String PAGE_CHANGES_WATCH_MAX_MILLS = "page.changes.watch.max.mills";
    private static final String PAGE_CHANGES_WATCH_INTERVAL_MILLS = "page.changes.interval.mills";
    private static final String PAGE_LOAD_TIME_OUT_MILLS = "page.load.time.out.mills";
    private static final String SCRIPT_LOAD_TIME_OUT_MILLS = "script.load.time.out.mills";
    private static final String WEBDRIVER_IMPLICIT_WAIT_TIME_OUT_MILLS = "webdriver.implicit.wait.time.out.mills";

    private static Config config = null;

    public static Config getConfig() {
        if (config  == null) {
            config = new Config();
        }
        return config;
    }

    @Getter private String rootPath;
    @Getter private String resourcesPath;
    @Getter private String env;
    @Getter private int pageChangesNotStartedMaxMills;
    @Getter private int pageChangesWatchMaxMills;
    @Getter private int pageChangesWatchIntervalMills;
    @Getter private  int pageLoadTimeOutMills;
    @Getter private int scriptLoadTimeOutMills;
    @Getter private int webDriverImplicitWaitTimeOutMills;
    @Getter private String chromeDriverPath;

    private Config() {
        env = System.getProperty("env");
        if (env == null) {
            System.err.println("env is not provided. Please set -Denv=<folder> what is /resources/env/<folder> ");
            System.exit(-1);
        }

        rootPath = System.getProperty("ROOT");

        if (rootPath == null) rootPath = System.getProperty("user.dir");
        resourcesPath = rootPath+ "/resources";

        String defaultProps = resourcesPath + "/env";
        String envProps = defaultProps+"/"+env;
        loadProperties(defaultProps);
        loadProperties(envProps);
        setTimingVars();
        setChromeDriverPath();
    }

    private void setChromeDriverPath() {
        String defValue = System.getProperty("user.dir")+"/Selenium/ChromeDriver/chromedriver";
        chromeDriverPath = System.getProperty("webdriver.chrome.driver",defValue);
    }

    private void loadProperties(String prop_path) {
        if (prop_path.isEmpty()) {
            ResourceUtils.loadProperties();
        } else {
            ResourceUtils.loadProperties(prop_path);
        }
    }

    private void setTimingVars() {
        pageChangesNotStartedMaxMills = getInegerProperty(PAGE_CHANGES_NOT_STARTED_MAX_MILLS,"60");
        pageChangesWatchIntervalMills = getInegerProperty(PAGE_CHANGES_WATCH_INTERVAL_MILLS,"100");
        pageChangesWatchMaxMills = getInegerProperty(PAGE_CHANGES_WATCH_MAX_MILLS,"1000");
        pageLoadTimeOutMills = getInegerProperty(PAGE_LOAD_TIME_OUT_MILLS,"10000");
        scriptLoadTimeOutMills = getInegerProperty(SCRIPT_LOAD_TIME_OUT_MILLS,"5000");
        webDriverImplicitWaitTimeOutMills = getInegerProperty(WEBDRIVER_IMPLICIT_WAIT_TIME_OUT_MILLS,"10000");
    }

    private int getInegerProperty(String propName, String defaultValue) {
        String val = System.getProperty(propName,defaultValue);
        return Integer.parseInt(val);
    }

}

