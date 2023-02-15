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
    @BeforeMethod       //для работы с внешним почт.сервером закомментить
    public void startMailServer() {
        app.mail().start();
    }

    @Test
    public void testRegistration() throws IOException, MessagingException, InterruptedException {

        //ф-ия возвращает текущее время в мс от 01.01.1970
        long now = System.currentTimeMillis();

        //имя юзера будет уникальным с добавленной переменной now
        String user = String.format("user%s", now);
        String password = "password";

        //уникальную переменную добавили также в почту
        String email = String.format("user%s@localhost.localdomain", now);

        //создание юзера на внешнем почт.сервере
        //app.james().createUser(user, password);            //для работы с внешним почт.сервером закомментить

        /*
        //1ая часть регистрации юзера, после чего должно прийти письмо
        app.registration().start(user, email);

        //получение письма из встроенного почт.сервера
        List<MailMessage> mailMessages = app.mail().waitForMail(2,10000);

        //получение письма из внешнего почт.сервера
        //List<MailMessage> mailMessages = app.james().waitForMail(user, password, 60000);

        //найти все письма которые пришли этому пользователю и извлечь сссылку из письма
        String confirmationLink = findConfirmationLink(mailMessages, email);

        */
        app.registration().registrationNewUser(email, user, password);

        //проверка что после регистрации юзер может войти в си-му
        assertTrue(app.newSession().login(user, password));
    }

    /*
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
    */

    //alwaysRun = true - всегда выполнять, даже когда тест упал
    @AfterMethod(alwaysRun = true)       //для работы с внешним почт.сервером закомментить
    public void stopMailServer() {
        app.mail().stop();
    }
    }
