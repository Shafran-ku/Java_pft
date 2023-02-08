package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

public class RegistrationTests extends TestBase{

    //перед началом тестов запускаем заново почтовый сервер, чтобы старая почта пропадала
    @BeforeMethod
    public void startMailServer() {
        app.mail().start();
    }

    @Test
    public void testRegistration() throws MessagingException, IOException {

        String user = "user10";
        String password = "password";
        String email = "user10@localhost.localdomain";
        app.registration().start(user, email);
        List<MailMessage> mailMessages = app.mail().waitForMail(2,10000);
        //найти все письма которые пришли этому пользователю и извлечь сссылку из письма
        String confirmationLink = findConfirmationLink(mailMessages, email);

        app.registration().finish(confirmationLink, password);

        //проверка что после регистрации юзер может войти в си-му
        assertTrue(app.newSession().login(user, password));
    }

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


    //alwaysRun = true - всегда выполнять, даже когда тест упал
    @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
    }
