package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.WebDriver;

public class RegistrationHelper {
    private final ApplicationManager app;
    private WebDriver wd;

    public RegistrationHelper(ApplicationManager app) {
        this.app = app;
        //ленивая инициализация - запуск браузера
        wd = app.getDriver();
    }

    public void start(String Username, String email) {
        wd.get(app.getProperty("web.baseUrl") + "/signup_page.php");
    }
}
