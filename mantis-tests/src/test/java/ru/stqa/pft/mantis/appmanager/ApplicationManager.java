package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {
    private final Properties properties;
    private WebDriver wd;

    private String browser;
    private RegistrationHelper registrationHelper;
    private FtpHelper ftp;


    public ApplicationManager(String browser) throws IOException {
        this.browser = browser;
        properties = new Properties();
    }

    //при вызове метода init только загружается конфиг-ый файл
    public void init() throws IOException {
        //загрузка свойств из конфигурационного файла (local.properties)

        //local - название конфигур-го файла
        String target = System.getProperty("target", "local");

        //вместо %s будет подставлен target
        properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
    }

    public void stop() {
        //если не null то остановить
        if (wd !=null ) {
            wd.quit();
        }
    }

    //метод инициализирует помощника при каждом обращении (можно открывать много сессий, т.к. помощник легковесный)
    //т.е. тесты будут выполняться не в браузере, а на уровне сетевого протокола
    public HttpSession newSession() {
        return new HttpSession(this);
    }

    //в качестве параметра принимает имя того свойства которое надо извлечь
    public String getProperty(String key) {             //не работало когда было public Object getProperty(String key)
        return properties.getProperty(key);
    }

    public RegistrationHelper registration() {
        //
        if (registrationHelper == null) {
            registrationHelper = new RegistrationHelper(this);
        }
        return registrationHelper;
    }

    //this - ссылка на application manager
    public FtpHelper ftp() {
        //если помощник не иниициализирован, то менеджер его иниц-ет
        if (ftp == null) {
            ftp = new FtpHelper(this);
        } return ftp;
    }

    public WebDriver getDriver() {
        //ленивая инициализация
        //если драйвер не проинициализирован, то проинициализировать и вернуть
        if (wd == null) {
            if (browser.equals(BrowserType.FIREFOX)) {
                wd = new FirefoxDriver();
            } else if (browser.equals(BrowserType.CHROME)) {
                wd = new ChromeDriver();
            } else if (browser.equals(BrowserType.IE)) {
                wd = new InternetExplorerDriver();
            }
            wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            //использование загруженных свойств из конфигурационного файла (local.properties)
            wd.get(properties.getProperty("web.baseUrl"));
        }
        return wd;
    }
}