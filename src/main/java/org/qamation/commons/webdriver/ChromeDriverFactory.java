package org.qamation.commons.webdriver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.qamation.commons.config.Config;

import java.net.URL;

public class ChromeDriverFactory {

    public static ChromeDriver createLocalChromeDriver(String pathToExec) {
        System.setProperty("webdriver.chrome.driver",pathToExec);
        ChromeOptions chromeOptions = getChromeOptions();
        return new ChromeDriver(chromeOptions);
    }

    public static ChromeDriver createLocalChromeDriver(String pathToExec, ChromeOptions chromeOptions) {
        System.setProperty("webdriver.chrome.driver",pathToExec);
        return new ChromeDriver(chromeOptions);
    }

    public static ChromeDriver createLocalChromeDriver() {
        String pathToExec = getChromeDriverPath();
        return createLocalChromeDriver(pathToExec);
    }
    public static ChromeDriver createLocalChromeDriver(ChromeOptions chromeOptions) {
        String pathToExec = getChromeDriverPath();
        return createLocalChromeDriver(pathToExec, chromeOptions);
    }

    public static WebDriver createRemoteChromeDriver(URL hub, ChromeOptions chromeOptions) {
        return createRemoteWebdriver(hub, new DesiredCapabilities(chromeOptions));
    }

    public static WebDriver createRemoteChromeDriver(URL hub) {
        ChromeOptions chromeOptions = getChromeOptions();
        return createRemoteWebdriver(hub, new DesiredCapabilities(chromeOptions));
    }

    public static RemoteWebDriver createRemoteWebdriver(URL hub, DesiredCapabilities capabilities) {
        addDefaultCapabilitiesTo(capabilities);
        return new RemoteWebDriver(hub, capabilities);
    }

    public static String getChromeDriverPath() {
        return Config.getConfig().getChromeDriverPath();
    }

    public static ChromeOptions getChromeOptions() {
        MutableCapabilities mc = getDefaultMutualCapabilities();
        return getChromeOptionsFrom(mc);
    }


    private static ChromeOptions getChromeOptionsFrom(MutableCapabilities mc) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(mc);
        chromeOptions = chromeOptions.addArguments("--disable-extensions");
        return chromeOptions;
    }

    private static void addDefaultCapabilitiesTo(DesiredCapabilities dc) {
        dc.merge(getDefaultMutualCapabilities());
    }

    public static MutableCapabilities getDefaultMutualCapabilities() {
        MutableCapabilities mc = new MutableCapabilities();
        mc.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        return mc;
    }





}
