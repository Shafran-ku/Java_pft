package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

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

    //submit
    public void submitLink(String email, String user, String password) throws InterruptedException, MessagingException, IOException {
        List<MailMessage> mailMessages = app.mail().waitForMail(2, 1000);
        String confirmationLink = findConfirmationLink(mailMessages, email);
        app.user().confirmAccount(confirmationLink, password, user);
    }

    //получаем ссылку подтверждения
    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findAny().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    public void registrationNewUser(String email, String user, String password) throws InterruptedException, MessagingException, IOException {
        start(user, email);
        submitLink(email, user, password);

    }
}