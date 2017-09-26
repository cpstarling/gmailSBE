import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Keys;
import java.awt.Robot;





@RunWith(ConcordionRunner.class)
public class sendEmailValidationTest {
    public WebDriver driver;
    public boolean authenticated;

    public WebDriver getDriver(){
        if (driver == null){
            //System.setProperty("webdriver.chrome.driver", "/home/chris/workspace/webdriver/chromedriver");
		        this.driver = new ChromeDriver();
            return driver;
        }
        else {
            return this.driver;
        }
      }

    public boolean authenticateToInbox() {
        if (!authenticated) {
            if (gmailLogin("sbe.automation@gmail.com")) {
                if (gmailPassword("boguspassword")) {
                    authenticated = true; }
            }
        }
        else {
            driver = this.getDriver();
            //driver.get("http://www.gmail.com");
            //check for alert (navigate way) and accept
            try {
                driver.switchTo().alert().accept();}
            catch (NoAlertPresentException e) {
            }

        }
        return waitForPageTitleContains("Inbox", 2);
    }

    public boolean gmailLogin(String username){
        driver = this.getDriver();
        //hit gmail
        driver.get("http://www.gmail.com");

        //enter userid
        driver.findElement(By.name("identifier")).sendKeys(username);
        driver.findElement(By.id("identifierNext")).click();

        return waitForElementById("profileIdentifier", 10);

    }


    public boolean gmailPassword(String password){
        driver = this.getDriver();
        //enter userid
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("passwordNext")).click();


        return waitForPageTitleContains("Inbox", 10);
    }


  public String sendEmail(String to, String subject, String body){
    driver = this.getDriver();
    authenticateToInbox();
    String returnValue = "";

    // expect to be at inbox
    if (!waitForPageTitleContains("Inbox", 0)) { returnValue = "Not at inbox"; }
    else {
        //click compose button
        driver.findElement(By.cssSelector("div[gh=cm]")).click();

        //wait for compose screen to show up
        if (!waitForElementByName("to", 1)) { returnValue = "Couldn't open email compose screen"; }
        else {
            //enter email details
            if (to != null){
              driver.findElement(By.name("to")).sendKeys(to);
            }
            if (subject != null){
              driver.findElement(By.name("subjectbox")).sendKeys(subject);
            }

            WebElement bodyElement = driver.findElement(By.cssSelector("div[aria-label='Message Body']"));

            if (body != null){
              bodyElement.sendKeys(body);
            }

           if (waitForClickableByXpath("//div[starts-with(@data-tooltip,'Send')]/parent::div/div[2]", 1)) {
                WebElement submit = driver.findElement(By.xpath("//div[starts-with(@data-tooltip,'Send')]/parent::div/div[2]"));
                submit.click();
                if (waitForElementByXpath("//div[contains(text(), 'message has been sent')]", 1)) {
                    returnValue = "Sent";
                }
                else { //check for warningn and return message
                    try {
                        returnValue = driver.findElement(By.xpath("//div[@role='alertdialog']/div[2]")).getText();
                    }
                    catch (Exception e) {
                        try {
                            returnValue = driver.switchTo().alert().getText();
                        }
                        catch (Exception unknown) {
                            returnValue = "Unknown State";
                        }
                    }

                }

           } else {
                returnValue = "Not Sendable";
           }
        }
    }
    return returnValue;
  }

  public boolean checkAttachment (String warningMessage, String body){
    String result = sendEmail("validAddress@mailinator.com","Valid subject", body);
    if(result.contains("It seems like you have forgotten to attach a file")){
      return true;
    } else {
      return false;
    }

  }


  public void createNewIssue(String summary, String description, String priority){
      //home.createNewIssue(summary, description, priority);
  }

  public boolean isIssueCreated(){
      //return home.isNewIssueCreated();
      return true;
  }

  public void cleanUp() {
      driver = this.getDriver();
      driver.quit();
  }

  public boolean waitForPageTitleContains(String pageTitle, Integer waitDuration) {
     driver = this.getDriver();
     WebDriverWait wait = new WebDriverWait(driver, waitDuration);

     try {
         wait.until(ExpectedConditions.titleContains(pageTitle));
     }
     catch (TimeoutException e) {
         //cleanUp();
         return false;
     }

     return true;
  }

  public boolean waitForElementByName(String name, Integer waitDuation) {
      WebDriverWait wait = new WebDriverWait(driver, waitDuation);
      try {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
      } catch (TimeoutException timeout) { return false; }
      return true;
  }

  public boolean waitForElementById(String id, Integer waitDuation) {
      WebDriverWait wait = new WebDriverWait(driver, waitDuation);
      try {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
      } catch (TimeoutException timeout) { return false; }
      return true;
  }

  public boolean waitForElementByXpath(String xpath, Integer waitDuation) {
      WebDriverWait wait = new WebDriverWait(driver, waitDuation);
      try {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
      } catch (TimeoutException timeout) { return false; }
      return true;
  }


  public boolean waitForClickableByXpath(String xpath, Integer waitDuation) {
      WebDriverWait wait = new WebDriverWait(driver, waitDuation);
      try {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
      } catch (TimeoutException timeout) { return false; }
      return true;
  }

}
