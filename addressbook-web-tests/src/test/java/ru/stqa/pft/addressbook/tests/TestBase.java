package ru.stqa.pft.addressbook.tests;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

//перед каждым тестовым методом инициализирует объект типа ApplicationManager, после того как метод отработал - разрушает его
public class TestBase {

    //создаем логгер и указываем класс TestBase, с которым логгер будет ассоциирован
    Logger logger = LoggerFactory.getLogger(TestBase.class);

    //указали системное свойство тип браузера - он указывается в конфиге; если там не указан то по-умолчанию CHROME (BrowserType)
    protected static final ApplicationManager app;    //выбор нужного браузера IE CHROME FIREFOX

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
    public void logTestStop(Method m) {
        //тормозим логгер
        logger.info("Stop test " + m.getName());

    }

    public void verifyGroupListInUI() {
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        //Boolean.getBoolean() - получает свойство и преобразует его в булево
        if (Boolean.getBoolean("verifyUI")) {
            //множество загружаемые из БД
            Groups dbGroups = app.db().groups();

            //множество загружаемый из UI
            Groups uiGroups = app.group().all();

            //и сравниваем оба множества
            //из множества групп из БД удаляем (map) инф-ию о header и footer, оставляем только id и GroupName, как в UI (collect)
            assertThat(uiGroups, equalTo(dbGroups.stream()
                    .map((g) -> new GroupData().withId(g.getId()).withName(g.getName()))
                    .collect(Collectors.toSet())));
        }
    }

    public void verifyContactListInUI() {
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        //Boolean.getBoolean() - получает свойство и преобразует его в булево
        if (Boolean.getBoolean("verifyUI")) {
            //множество загружаемые из БД
            Contacts dbContacts = app.db().contacts();

            //множество загружаемый из UI
            Contacts uiContacts = app.contact().all();

            //и сравниваем оба множества
            //из множества контактов из БД удаляем (map) ненужную инф-ию, оставляем только id,Firstname и Lastname, как в UI (collect)
            assertThat(uiContacts, equalTo(dbContacts.stream()
                    .map((g) -> new ContactData()
                            .withId(g.getId()).withFirstname(g.getFirstname()).withLastname(g.getLastname())
                            .withAddress(g.getAddress()).withEmail(g.getEmail()))
                    .collect(Collectors.toSet())));
        }
    }

}
