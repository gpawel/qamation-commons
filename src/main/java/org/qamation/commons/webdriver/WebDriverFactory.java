package org.qamation.commons.webdriver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class WebDriverFactory {

    public static Logger log = LoggerFactory.getLogger(WebDriverFactory.class);

	public static WebDriver createRemoteEdgeDriver(URL hub) {
		DesiredCapabilities dc =  setEdgeCapabilities();
		RemoteWebDriver driver = createRemoteWebdriver(hub, dc);
		return driver;
	}

	public static WebDriver createRemoteChromeDriver(URL hub) {
		return ChromeDriverFactory.createRemoteChromeDriver(hub);
	}
	public static WebDriver craeatRemoteChromeDriver(URL hub, ChromeOptions chromeOptions) {
		return ChromeDriverFactory.createRemoteChromeDriver(hub,chromeOptions);
	}

	public static WebDriver createChromeWebDriver(String path) {
		WebDriver driver = ChromeDriverFactory.createLocalChromeDriver(path);
		return driver;
	}

	public static WebDriver createChromeWebDriver(String path, ChromeOptions chromeOptions) {
		WebDriver driver = ChromeDriverFactory.createLocalChromeDriver(path, chromeOptions);
		return driver;
	}

	public static WebDriver createChromeWebDriver() {
		return ChromeDriverFactory.createLocalChromeDriver();
	}

	public static MutableCapabilities getDefaultMutualCapabilities() {
		MutableCapabilities mc = new MutableCapabilities();
		mc.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
		return mc;
	}

	private static String getOSName() {
		return System.getProperty("os.name");
	}

	public static WebDriver createEdgeWebDriver(String path) {
		EdgeOptions edgeOptions = new EdgeOptions();
		edgeOptions.setBinary(path);
		DesiredCapabilities dc = setEdgeCapabilities();
		edgeOptions.merge(dc);
		WebDriver driver = new EdgeDriver(edgeOptions);
		return driver;
	}

	public static WebDriver createFFWebDriver(String path) {
		FirefoxOptions options = new FirefoxOptions();
		options.setBinary(path);
		DesiredCapabilities dc = setFFCapabilities();

		options.merge(dc);
		WebDriver driver = new FirefoxDriver(options);
		return driver;
	}

    public static WebDriver createRemoteFFWebDriver(URL hub) {
		DesiredCapabilities dc = setFFCapabilities();
		RemoteWebDriver driver = createRemoteWebdriver(hub,dc);
		return driver;
    }

	public static RemoteWebDriver createRemoteWebdriver(URL hub, DesiredCapabilities capabilities) {
		addDefaultCapabilitiesTo(capabilities);
		RemoteWebDriver driver = 	new RemoteWebDriver(hub, capabilities);
		return driver;
	}

	private static DesiredCapabilities setFFCapabilities() {
		DesiredCapabilities dc = new DesiredCapabilities();
		addDefaultCapabilitiesTo(dc);
		return dc;
	}

	private static DesiredCapabilities setEdgeCapabilities() {
		DesiredCapabilities dc = new DesiredCapabilities();
		addDefaultCapabilitiesTo(dc);
		return dc;
	}

	private static void addDefaultCapabilitiesTo(DesiredCapabilities dc) {
		dc.merge(getDefaultMutualCapabilities());
	}












}
