import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
@RunWith(ConcordionRunner.class)
public class loginSuccessfullyTest {
    public WebDriver driver;

    public WebDriver getDriver(){
        if (driver == null){
            System.setProperty("webdriver.chrome.driver", "/home/chris/workspace/webdriver/chromedriver");
		    WebDriver driver = new ChromeDriver();
          return driver;
        }else{
          return driver;
        }
      }

    public boolean gmailLogin(String username){
        driver = this.getDriver();
        //hit gmail        
        driver.get("http://www.gmail.com");

        //enter userid
        driver.findElement(By.name("identifier")).sendKeys(username);
        driver.findElement(By.id("identifierNext")).click();


        //wait for profile screen
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("profileIdentifier"))); 

        return true;
        //not necassary, but good for reference
        //WebElement profileElement = driver.findElement(By.id("profileIdentifier"));
        //return profileElement.getText().equals(username);
 
    }


    public boolean gmailPassword(String password){
        driver = this.getDriver();
        //enter userid
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("passwordNext")).click();


        //wait for inbox 
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleContains("Inbox")); 

        //if get here then we loaded inbox, otherwise wait condition fails
        return true;
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
        driver.close();
    }

}		


