package ru.stqa.pft.addressbook;

import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class GroupCreationTests {
  private WebDriver webDriver;

  @BeforeMethod(alwaysRun = true)
  public void setUp() throws Exception {
    webDriver = new FirefoxDriver();
    webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testGroupCreation() throws Exception {
    webDriver.get("http://localhost/addressbook/group.php");
    webDriver.findElement(By.name("user")).click();
    webDriver.findElement(By.name("user")).clear();
    webDriver.findElement(By.name("user")).sendKeys("admin");
    webDriver.findElement(By.name("pass")).clear();
    webDriver.findElement(By.name("pass")).sendKeys("secret");
    webDriver.findElement(By.xpath("//input[@value='Login']")).click();
    webDriver.findElement(By.linkText("groups")).click();
    webDriver.findElement(By.name("new")).click();
    webDriver.findElement(By.name("group_name")).click();
    webDriver.findElement(By.name("group_name")).clear();
    webDriver.findElement(By.name("group_name")).sendKeys("test1");
    webDriver.findElement(By.name("group_header")).clear();
    webDriver.findElement(By.name("group_header")).sendKeys("test2");
    webDriver.findElement(By.name("group_footer")).clear();
    webDriver.findElement(By.name("group_footer")).sendKeys("test3");
    webDriver.findElement(By.name("submit")).click();
    webDriver.findElement(By.linkText("group page")).click();
    webDriver.findElement(By.linkText("Logout")).click();
  }

  @AfterMethod(alwaysRun = true)
  public void tearDown() throws Exception {
    webDriver.quit();
   }

  private boolean isElementPresent(By by) {
    try {
      webDriver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      webDriver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

 }

