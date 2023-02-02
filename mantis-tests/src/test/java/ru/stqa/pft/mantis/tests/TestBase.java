package ru.stqa.pft.mantis.tests;

import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.mantis.appmanager.ApplicationManager;

import java.io.IOException;

//перед каждым тестовым методом инициализирует объект типа ApplicationManager, после того как метод отработал - разрушает его
public class TestBase {

    //указали системное свойство тип браузера - он указывается в конфиге;
    // если там не указан то по-умолчанию CHROME (BrowserType)
    protected static final ApplicationManager app;   //выбор нужного браузера IE CHROME FIREFOX

    static {
        try {
            app = new ApplicationManager(System.getProperty("browser", BrowserType.FIREFOX));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @BeforeSuite //глобальная инициализация. (Suite может состоять из нескольких тестов)
    public void setUp() throws Exception {
        app.init();
    }

    //(alwaysRun = true) чтобы браузер останавливался всегда
    @AfterSuite(alwaysRun = true)
    public void tearDown() throws Exception {
        app.stop();
    }

}
