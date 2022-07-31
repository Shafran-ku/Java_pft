package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;

//перед каждым тестовым методом инициализирует объект типа ApplicationManager, после того как метод отработал - разрушает его
public class TestBase {

    protected final ApplicationManager app = new ApplicationManager(BrowserType.IE);    //выбор нужного браузера IE CHROME FIREFOX

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        app.init();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {
        app.stop();
    }

}
