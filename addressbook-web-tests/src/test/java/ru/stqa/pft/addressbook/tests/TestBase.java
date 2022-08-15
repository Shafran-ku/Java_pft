package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;

//перед каждым тестовым методом инициализирует объект типа ApplicationManager, после того как метод отработал - разрушает его
public class TestBase {

    protected static ApplicationManager app = new ApplicationManager(BrowserType.CHROME);    //выбор нужного браузера IE CHROME FIREFOX

    @BeforeSuite //глобальная инициализация. (Suite может состоять из нескольких тестов)
    public void setUp() throws Exception {
        app.init();
    }

    @AfterSuite
    public void tearDown() throws Exception {
        app.stop();
    }

}
