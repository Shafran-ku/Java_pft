package ru.stqa.pft.mantis.tests;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;

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

    @Test
    public void testChangePassword() throws IOException, MessagingException {

        //заходим под админом
        app.login().loginAdmin();

        //переходим на Manage
        app.login().manageUsers(By.xpath("//div[@id='sidebar']/ul/li[6]/a/i"));

        //нашли юзера
        String username = String.format(app.getDriver().findElement(By.xpath("//tbody/tr[2]/td[1]/a")).getText());

        //берем почт.адрес
        String email1 = String.format(app.getDriver().
                findElement(By.xpath("//tbody/tr[2]/td[3]")).getText());
        String password1 = "root1";

        //зашли в юзера и нажали Reset Password
        app.login().click(By.linkText(String.format("%s", username)));
        app.login().click(By.cssSelector("input[value='Reset Password'"));

        //проверить почту
        List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
        //найти ссылку подтверждения
        String confirmationLink = findConfirmationLink(mailMessages, email1);

        //пройти по ссылке, ввести новый пароль
        app.registration().finish(confirmationLink, password1, username);

        //проверить вход с новым паролем
        assertTrue(app.newSession().login(username, password1));
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
