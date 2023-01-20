package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

//перед каждым тестовым методом инициализирует объект типа ApplicationManager, после того как метод отработал - разрушает его
public class TestBase {

    //создаем логгер и указываем класс TestBase, с которым логгер будет ассоциирован
    Logger logger = LoggerFactory.getLogger(TestBase.class);

    //указали системное свойство тип браузера - он указывается в конфиге; если там не указан то по-умолчанию CHROME (BrowserType)
    protected static final ApplicationManager app;    //выбор нужного браузера IE CHROME FIREFOX

    static {
        try {
            app = new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));
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

    //метод инициализации, выполняется перед каждым тестовым методом
    //(alwaysRun = true) - указание запускаться всегда, даже если тестовый метод упадет
    @BeforeMethod(alwaysRun = true)
    //(Method m) - передача инф-ии о тестовом методе. который запускается
    public void logTestStart(Method m, Object[] p) {
        //стартуем логгер + выводим инф-ию о параметрах запуска
        logger.info("Start test " + m.getName() + " with parameters " + Arrays.asList(p));
    }

    //выводится сообщение что тестовый метод завершил работу
    @AfterMethod(alwaysRun = true)
    public void logTestStop(Method m){
        //тормозим логгер
        logger.info("Stop test " + m.getName());

    }


}
