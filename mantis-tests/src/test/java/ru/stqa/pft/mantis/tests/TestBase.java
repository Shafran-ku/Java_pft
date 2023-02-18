package ru.stqa.pft.mantis.tests;

import biz.futureware.mantis.rpc.soap.client.IssueData;
import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.mantis.appmanager.ApplicationManager;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

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
        //инициализация ()
        //параметры: 1-файл, который дб загружен на удаленную машину,2-имя файла, куда загружается,
        //3-имя резервной копии, если удаленный файл уже существует
        app.ftp().upload(new File("src/test/resources/config_inc.php"), "config_inc.php", "config_inc.php.bak");
    }

    //функция, через Remote API получать из баг-трекера информацию о баг-репорте с заданным идентификатором,
    // и возвращать значение false (баг исправлен) или true(ба не исправлен)
    public boolean isIssueOpen(int issueId) throws MalformedURLException, ServiceException, RemoteException {
        IssueData issue = app.soap().getIssue(issueId);
        if (issue.getStatus().getName().equals("resolved") || issue.getStatus().getName().equals("closed")) {
            return false;
        }
        return true;
    }

    //ф-ия, вызывается в начале нужного теста, чтобы он пропускался, если баг ещё не исправлен
    public void skipIfNotFixed(int issueId) throws MalformedURLException, ServiceException, RemoteException {
        if (isIssueOpen(issueId)) {
            System.out.println("ignored because of issue " + issueId);
        }
    }

    //(alwaysRun = true) чтобы браузер останавливался всегда
    @AfterSuite(alwaysRun = true)
    public void tearDown() throws Exception {

        //восстанавливаем файл config_inc.php.bak с его оригинальным названием config_inc.php
        app.ftp().restore("config_inc.php.bak", "config_inc.php");
        app.stop();
    }

}
