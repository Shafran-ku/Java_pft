package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationHelper extends HelperBase {

    public RegistrationHelper(ApplicationManager app) {
        //конструктор базового класса, куда передается ссылка на ApplicationManager
        super(app);
    }

    //начало регистрации
    public void start(String username, String email) {
        wd.get(app.getProperty("web.baseUrl") + "/signup_page.php");

        //принимает локатор элемента и текст, который нужно вести в соответствующе поле
        type(By.name("username"), username);
        type(By.name("email"), email);

        //жмем sign up (ищем элемент input, у которого value='Signup')
        click(By.cssSelector("input[value='Signup']"));

    }

    //завершение регистрации пользователя
    public void finish(String confirmationLink, String password) {
        //проходим по ссылке
        wd.get(confirmationLink);

        //заполняем 2 поля
        type(By.name("password"), password);
        type(By.name("password_confirm"), password);

        //нажать Update User
        click(By.cssSelector("input[value='Update User']"));
    }
}
