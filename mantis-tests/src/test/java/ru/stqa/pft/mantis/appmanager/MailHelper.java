package ru.stqa.pft.mantis.appmanager;


import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MailHelper {
    private ApplicationManager app;
    private final Wiser wiser;

    public MailHelper(ApplicationManager app) {
        this.app = app;
        //при иниц-ии создается объкт типа Wiser- почтовый сервер
        wiser = new Wiser();
    }

    //ожидание; count - кол-во писем которое д прийти, timeout - время ожидания
    public List<MailMessage> waitForMail(int count, long timeout) throws MessagingException, IOException  {
    //запоминаем текущее время
    long start = System.currentTimeMillis();
    //цикл что текущее время не превышаем время старта + таймаут
    while(System.currentTimeMillis() < start + timeout) {
        //проверяем пришло ли что-нибудь
        if (wiser.getMessages().size() >= count) {
            //преобразование реальных объектов в модельные, с которыми мы работаем:
            // список превращаем в поток, ко всем элементам потока применяем ф-ию toModelMaii,
            // далее объекты собирам снова в список
            return wiser.getMessages().stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
        }
        //если нет подождать еще
        try {Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //сообщение что почты нет
    throw new Error("No mail :(");
}
    public static MailMessage toModelMail(WiserMessage m) {
        try {
            MimeMessage mm = m.getMimeMessage();
            //берем реальный объект (письмо) и первого получателя
            return new MailMessage(mm.getAllRecipients()[0].toString(), (String) mm.getContent());
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //запуск почтового сервера
    public void start() {
        wiser.start();
    }

    //остановка
    public void stop() {
        wiser.stop();
    }

    public List<MailMessage> getMailMessages() throws MessagingException, IOException {
        List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
        return mailMessages;
    }
}