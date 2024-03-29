package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {
    private final Properties properties;
    WebDriver wd;
    private ContactHelper contactHelper;
    private SessionHelper sessionHelper;
    private NavigationHelper navigationHelper;
    private GroupHelper groupHelper;
    private String browser;
    private DbHelper dbHelper;

    public ApplicationManager(String browser) throws IOException {
        this.browser = browser;
        properties = new Properties();
    }

    public void init() throws IOException {
        //загрузка свойств из конфигурационного файла (local.properties)

        //local - название конфигур-го файла
        String target = System.getProperty("target", "local");

        //вместо %s будет подставлен target
        properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));

        //иниициализация помощника работы c БД
        dbHelper = new DbHelper();

        //если свойство selenium.server в remote.properties пустая строка, то инициализация такая же как и раньше,
        //иначе использовать другой тип драйвера
        if("".equals(properties.getProperty("selenium.server"))) {
            if (browser.equals(BrowserType.FIREFOX)) {
                wd = new FirefoxDriver();
            } else if (browser.equals(BrowserType.CHROME)) {
                wd = new ChromeDriver();
            } else if (browser.equals(BrowserType.IE)) {
                wd = new InternetExplorerDriver();
            }
        } else {
            //запуск тестов удаленно
            //capabilities - тип браузера. который мы хотим использовать
            DesiredCapabilities capabilities = new DesiredCapabilities();
            //устанавливаем браузер
            capabilities.setBrowserName(browser);
            //устанавливаем желаемую платформу для запуска тестов
            capabilities.setPlatform(Platform.fromString(System.getProperty("platform", "win7")));

            wd = new RemoteWebDriver(new URL(properties.getProperty("selenium.server")), capabilities);

        }


        wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        //использование загруженных свойств из конфигурационного файла (local.properties)
        wd.get(properties.getProperty("web.baseUrl"));

        contactHelper = new ContactHelper(wd);
        groupHelper = new GroupHelper(contactHelper.wd);
        navigationHelper = new NavigationHelper(contactHelper.wd);
        sessionHelper = new SessionHelper(contactHelper.wd);

        //использование загруженных свойств из конфигурационного файла (local.properties)
        sessionHelper.login(properties.getProperty("web.adminLogin"), properties.getProperty("web.adminPassword"));
    }

    public void stop() {
        contactHelper.wd.quit();
    }

    public GroupHelper group() {
        return groupHelper;
    }

    public NavigationHelper goTo() {
        return navigationHelper;
    }

    public ContactHelper contact() {
        return contactHelper;
    }

    public DbHelper db() {
        return dbHelper;
    }

}
