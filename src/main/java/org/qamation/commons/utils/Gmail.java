package org.qamation.commons.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.qamation.commons.webdriver.IsPageReadyUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Gmail  {
  public static final String Gmail_URL ="https://Gmail.com";
  public static final String Gmail_SIGN_OUT="https://mail.google.com/mail/?logout&hl=en";

  private String currentTab = null;
  private String GmailTab = null;

  private By userNameLocator = By.xpath("//input[@id='identifierId']");
  private By nextButtonLocator = By.xpath("//button[@type='button']//span[text()='Next']");
  private By passwordFieldLocator = By.xpath("//input[@aria-label='Enter your password']");
  private By notNowButtonLocator = By.xpath("//button//span[text()='Not now']");
  private By inboxElementLocator = By.xpath("//a[text()='Inbox']");
  private By resetPasswordEmailLocator = By.xpath("(//tbody//span[text()='Password Reset'])[2]");

  private By selectAllCheckBoLocator = By.xpath("//*[@aria-label='Select']/div[1]/span");
  private By deleteButtonLocator = By.xpath("//div[@aria-label='Delete']/div");
  private By deleteConfirmationMessageLocator = By.xpath("//td[text()='No new mail!']");

  private By refreshInboxContentLocator = By.xpath("//div[@aria-label='Refresh']/div");

  private By selectInputToolLocator = By.xpath("//*[@aria-label='Select input tool']");

  private By resetURLLocator = By.xpath("//table//td//u[text()='Reset link']/..");

  private By chooseAccountLocator = By.xpath("//span[text()='Choose an account']");
  private By useAnotherAccountLocator = By.xpath("//div[text()='Use another account']");

  private WebDriver driver;
  private WebDriverWait wait;
  
  public Gmail (WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
  }

  public Gmail openGmailSite() {
    Set<String> tabs = driver.getWindowHandles();
    if (tabs.size() > 1) throw new RuntimeException("\nToo many tabs are opened. Make sure there is only one tab before opening Gmail in a new tab\n");
    currentTab = driver.getWindowHandle();
    GmailTab = openGmailTab();
    driver.get(Gmail_URL);
    checkChooseAccountWindow();
    wait.until(ExpectedConditions.elementToBeClickable(userNameLocator));
    wait.until(ExpectedConditions.elementToBeClickable(nextButtonLocator));
    return this;
  }

  private void checkChooseAccountWindow() {
    IsPageReadyUtils.isPageChangeStopped(this.driver);
    List<WebElement> els = driver.findElements(useAnotherAccountLocator);
    if (els.size() == 0) return;
    if (els.get(0).isDisplayed()) {
      els.get(0).click();
    }

  }

  public Gmail loginToGmail(String userName,String password) {
    enterUserName(userName);
    enterPassword(password);
    proceedToInbox();
    moveMouseTo(selectAllCheckBoLocator);
    return this;
  }

  public Gmail logout() {
    driver.get(Gmail_SIGN_OUT);
    wait.until(ExpectedConditions.visibilityOfElementLocated(chooseAccountLocator));
    wait.until(ExpectedConditions.elementToBeClickable(useAnotherAccountLocator));
    driver.findElement(useAnotherAccountLocator).click();
    return this;
  }


  public String openGmailTab() {
    JavascriptExecutor js = (JavascriptExecutor)driver;
    js.executeScript("window.open()");
    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
    String newWindowHandler = tabs.get(1);
    driver.switchTo().window(newWindowHandler);
    return newWindowHandler;
  }

  public Gmail closeGmailTab() {
    if (Objects.nonNull(GmailTab)) {
      driver.switchTo().window(GmailTab).close();
    }
    driver.switchTo().window(currentTab);
    GmailTab = null;
    return this;
  }

  public Gmail deleteAllEmailsFromInbox() {
    WebElement selectAllCheckBox = driver.findElement(selectAllCheckBoLocator);
    selectAllCheckBox.click();
    wait.until(ExpectedConditions.elementToBeClickable(deleteButtonLocator));
    WebElement deleteButton = driver.findElement(deleteButtonLocator);
    deleteButton.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(deleteConfirmationMessageLocator));
    return this;
  }

  public String readResetLink() {
    wait.until(resetPasswordEmailIsReceived(resetPasswordEmailLocator));
    WebElement resetPasswordEmailLink = driver.findElement(resetPasswordEmailLocator);
    resetPasswordEmailLink.click();
    wait.until(ExpectedConditions.elementToBeClickable(resetURLLocator));
    String resetLink = driver.findElement(resetURLLocator).getAttribute("href");
    return resetLink;
  }

  public void backToInbox() {
    driver.findElement(inboxElementLocator).click();
    waitGmailInboxIsLoaded();
  }

  private void checkAdditionalSecuritySettingsScreen() {
    List<WebElement> els = driver.findElements(notNowButtonLocator);
    if (els.size() > 0) {
      if (els.get(0).isDisplayed()) {
        els.get(0).click();
      }
    }
  }

  private void enterUserName(String userName) {
    WebElement userNameField = driver.findElement(userNameLocator);
    userNameField.sendKeys(userName);
    WebElement nextButtonEl = driver.findElement(nextButtonLocator);
    nextButtonEl.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(passwordFieldLocator));
    wait.until(ExpectedConditions.elementToBeClickable(passwordFieldLocator));
    wait.until(ExpectedConditions.visibilityOfElementLocated(nextButtonLocator));
    wait.until(ExpectedConditions.elementToBeClickable(nextButtonLocator));
  }

  private void enterPassword(String password) {
    WebElement passwordFieldEl = driver.findElement(passwordFieldLocator);
    passwordFieldEl.sendKeys(password);
  }

  private void proceedToInbox() {
    WebElement nextButtonEl = driver.findElement(nextButtonLocator);
    nextButtonEl.click();
    checkAdditionalSecuritySettingsScreen();
    waitGmailInboxIsLoaded();
  }

  private void waitGmailInboxIsLoaded() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(inboxElementLocator));
    wait.until(ExpectedConditions.elementToBeClickable(inboxElementLocator));
    wait.until(ExpectedConditions.elementToBeClickable(selectInputToolLocator));
    wait.until(ExpectedConditions.elementToBeClickable(selectAllCheckBoLocator));
    //wait.until(ExpectedConditions.elementToBeClickable(refreshInboxContentLocator));
  }

  private ExpectedCondition<WebElement> resetPasswordEmailIsReceived (final By locator) {
    return new ExpectedCondition<WebElement>() {

      @Override
      public WebElement apply(WebDriver input) {
        driver.findElement(refreshInboxContentLocator).click();
        List<WebElement> els = driver.findElements(locator);
        if (els.size() > 0) return els.get(0);
        else return null;
      }
    };
  }
  private void moveMouseTo(By elementLocator) {
      wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator));
      wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
      WebElement el = driver.findElement(elementLocator);
      Actions actions = new Actions(this.driver);
      actions.moveToElement(el).perform();
  }



}