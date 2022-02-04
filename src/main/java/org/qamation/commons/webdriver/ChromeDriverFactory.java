package org.qamation.commons.webdriver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.qamation.commons.utils.ResourceUtils;

import java.net.URL;

public class ChromeDriverFactory {

    public static ChromeDriver createChromeDriver(String pathToExec) {
        System.setProperty("webdriver.chrome.driver",pathToExec);
        ChromeOptions chromeOptions = getChromeOptions();
        return new ChromeDriver(chromeOptions);
    }

    public static ChromeDriver createChromeDriver(String pathToExec, ChromeOptions chromeOptions) {
        System.setProperty("webdriver.chrome.driver",pathToExec);
        return new ChromeDriver(chromeOptions);
    }

    public static ChromeDriver createChromeDriver() {
        String pathToExec = getChromeDriverPath();
        return createChromeDriver(pathToExec);
    }

    public static WebDriver createRemoteChromeDriver(URL hub, ChromeOptions chromeOptions) {
        RemoteWebDriver driver = createRemoteWebdriver(hub, new DesiredCapabilities(chromeOptions));
        return driver;
    }

    public static WebDriver createRemoteChromeDriver(URL hub) {
        ChromeOptions chromeOptions = getChromeOptions();
        RemoteWebDriver driver = createRemoteWebdriver(hub, new DesiredCapabilities(chromeOptions));
        return driver;
    }

    public static String getChromeDriverPath() {
        String defValue = System.getProperty("user.dir")+"/Selenium/ChromeDriver/chromedriver";
        return ResourceUtils.getSystemProperty("webdriver.chrome.driver",defValue);
    }

    public static ChromeOptions getChromeOptions() {
        MutableCapabilities mc = getDefaultMutualCapabilities();
        return getChromeOptionsFrom(mc);
    }

    private static ChromeOptions getChromeOptionsFrom(MutableCapabilities mc) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(mc);
        chromeOptions = chromeOptions.addArguments("--disable-extensions");
        chromeOptions.setExperimentalOption("useAutomationExtension", false);
        return chromeOptions;
    }

    private static DesiredCapabilities setChromeCapabilities() {
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options = options.addArguments("--disable-extensions");
        dc.setCapability(ChromeOptions.CAPABILITY, options);
        return dc;
    }

    public static RemoteWebDriver createRemoteWebdriver(URL hub, DesiredCapabilities capabilities) {
        addDefaultCapabilitiesTo(capabilities);
        RemoteWebDriver driver = 	new RemoteWebDriver(hub, capabilities);
        return driver;
    }

    private static void addDefaultCapabilitiesTo(DesiredCapabilities dc) {
        dc.merge(getDefaultMutualCapabilities());
    }

    public static MutableCapabilities getDefaultMutualCapabilities() {
        MutableCapabilities mc = new MutableCapabilities();
        mc.setCapability(org.openqa.selenium.remote.CapabilityType.ACCEPT_SSL_CERTS, true);
        mc.setCapability(org.openqa.selenium.remote.CapabilityType.SUPPORTS_JAVASCRIPT,true);
        mc.setCapability(org.openqa.selenium.remote.CapabilityType.SUPPORTS_FINDING_BY_CSS, true);
        return mc;
    }





}
