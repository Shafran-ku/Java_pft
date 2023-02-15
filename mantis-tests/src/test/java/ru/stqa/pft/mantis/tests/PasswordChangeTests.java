package ru.stqa.pft.mantis.tests;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;
import ru.stqa.pft.mantis.model.Users;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class PasswordChangeTests extends TestBase {

    //перед началом тестов запускаем заново почтовый сервер, чтобы старая почта пропадала
    @BeforeMethod
    public void startMailServer() {
        app.mail().start();
    }

    //проверка наличия юзера для теста - если всего 1 то создать нового
    @BeforeTest
    public void ensurePreconditions() throws InterruptedException, MessagingException, IOException {
        if (app.db().users().size() == 1) {
            app.registration().registrationNewUser("userNew@localhost.localdomain", "userNew", "password");
        }
    }

    @Test
    public void testChangePassword() throws IOException, MessagingException {

        //заходим под админом
        app.login().loginAdmin();

        //переходим на Manage
        app.login().manageUsers(By.xpath("//div[@id='sidebar']/ul/li[6]/a/i"));

        //берем юзера и его почту
        Users user = app.db().users();
        UserData userPassword = user.iterator().next();
        String username = userPassword.getUsername();
        String email = userPassword.getEmail();
        String passwordNew = "root";

        //зашли в юзера и нажали Reset Password
        app.login().click(By.linkText(String.format("%s", username)));
        app.login().click(By.cssSelector("input[value='Reset Password'"));

        //проверить почту
        List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
        //найти ссылку подтверждения
        String confirmationLink = findConfirmationLink(mailMessages, email);

        //пройти по ссылке, ввести новый пароль
        app.registration().finish(confirmationLink, passwordNew, username);

        //проверить вход с новым паролем
        assertTrue(app.newSession().login(username, passwordNew));
    }

    //вытаскиваем ссылку для подтверждения регистрации
    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        //фильтруем сообщения по нужному адресу
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();

        //из текста сообщения извлечь ссылку:
        // найти текст "http://",
        // потом nonSpace() - какое то кол-во непробельных символов, oneOrMore()- один или больше
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();

        //вернуть кусок текста, соответствующее построенному регулярному выражению
        return regex.getText(mailMessage.text);
    }

    //стопим сервер (alwaysRun = true - всегда выполнять, даже когда тест упал)
    @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
}
