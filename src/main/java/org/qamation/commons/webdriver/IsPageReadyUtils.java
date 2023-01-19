package org.qamation.commons.webdriver;

import org.openqa.selenium.*;
import org.qamation.commons.config.Config;
import org.qamation.commons.utils.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pavel.Gouchtchine on 12/30/2016.
 */
public class IsPageReadyUtils {
    private final static String PAGE_CHANGES_OBSERVER_ASYNC_SCRIPT = StringUtils.readFileIntoString("wait_page_changes_stop.js");
    private static Config conf = Config.getConfig();

    public static void setDriverTimeOuts(WebDriver driver, int pageLoadTOmills, int scriptLoadTOmills, int implicitTOmills) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(pageLoadTOmills));
        driver.manage().timeouts().scriptTimeout(Duration.ofMillis(scriptLoadTOmills));
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(implicitTOmills));
    }

    public static int isPageChangeStopped(WebDriver driver) {
        JavascriptExecutor js = getJavaScriptExecutor(driver);
        Long result = (Long) js.executeAsyncScript(PAGE_CHANGES_OBSERVER_ASYNC_SCRIPT,
                conf.getPageChangesNotStartedMaxMills(),
                conf.getPageChangesWatchMaxMills(),
                conf.getPageChangesWatchIntervalMills());
        return result.intValue();
    }


    public static JavascriptExecutor getJavaScriptExecutor(WebDriver driver) {
        if (driver instanceof JavascriptExecutor) {
            setDriverTimeOuts(driver,
                    conf.getPageLoadTimeOutMills(),
                    conf.getScriptLoadTimeOutMills(),
                    conf.getWebDriverImplicitWaitTimeOutMills()
            );
            return (JavascriptExecutor) driver;
        } else throw new RuntimeException("Cannot turn this driver into JavaScript");
    }






}
