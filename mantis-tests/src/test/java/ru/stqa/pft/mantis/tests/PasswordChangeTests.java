package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
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
        app.user().loginAdmin();

        //переходим на Manage
        app.user().selectManageUsers();

        //берем юзера и его почту
        Users user = app.db().users();
        UserData userPassword = user.iterator().next();
        String username = userPassword.getUsername();
        String email = userPassword.getEmail();

        String passwordNew = "root";

        //зайти в юзера и нажать Reset Password
        app.user().selectUser(username);
        app.user().resetPassword();

        //проверить почту
        List<MailMessage> mailMessages = app.mail().getMailMessages();

        //найти ссылку подтверждения в почте
        String confirmationLink = app.user().findConfirmationLink(mailMessages, email);

        //заходим по ссылке, вводим новый пароль
        app.user().confirmAccount(confirmationLink, passwordNew, username);

        //проверить вход с новым паролем
        assertTrue(app.newSession().login(username, passwordNew));
    }

    //стопим сервер (alwaysRun = true - всегда выполнять, даже когда тест упал)
    @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
}
