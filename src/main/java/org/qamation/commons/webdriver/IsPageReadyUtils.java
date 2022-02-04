package org.qamation.commons.webdriver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qamation.commons.config.Config;
import org.qamation.commons.utils.StringUtils;

import java.util.function.Function;

/**
 * Created by Pavel.Gouchtchine on 12/30/2016.
 */
public class IsPageReadyUtils {

    private final static String GET_DOCUMENT_CONTENT_ASYNC_SCRIPT = StringUtils.readFileIntoString("get_document_content_async.js");
    private final static String PAGE_CHANGES_OBSERVER_ASYNC_SCRIPT = StringUtils.readFileIntoString("wait_page_changes_stop.js");
    private final static String WINDOWS_LAODED_SCRIPT = StringUtils.readFileIntoString("wait_window_load.js");
// comment
    public static String getDocumentContent(WebDriver driver) {
        JavascriptExecutor js = getJavaScriptExecutor(driver);
        String content = (String) js.executeAsyncScript(GET_DOCUMENT_CONTENT_ASYNC_SCRIPT);
        return content;
    }

    public static int isPageChangeStopped(WebDriver driver) {
        JavascriptExecutor js = getJavaScriptExecutor(driver);
        Long result = (Long) js.executeAsyncScript(PAGE_CHANGES_OBSERVER_ASYNC_SCRIPT,
                Config.getPageChangesTimeOutMillis(),
                Config.getPageChangesWatchForMaxMillis(),
                Config.getPageChangesIntervalMillis());
        return result.intValue();
    }

    public static Long waitPageWindowLoaded(WebDriver driver) {
        JavascriptExecutor js = getJavaScriptExecutor(driver);
        Long duration = (Long) js.executeAsyncScript(WINDOWS_LAODED_SCRIPT);
        return duration;
    }

    public static JavascriptExecutor getJavaScriptExecutor(WebDriver driver) {
        if (driver instanceof JavascriptExecutor) {
            Config.setDriverTimeOuts(driver);
            return (JavascriptExecutor) driver;
        } else throw new RuntimeException("Cannot turn this driver into JavaScript");
    }




}
