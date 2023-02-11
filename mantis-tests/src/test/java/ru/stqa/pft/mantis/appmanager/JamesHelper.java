package ru.stqa.pft.mantis.appmanager;


import org.apache.commons.net.telnet.TelnetClient;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.Session;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JamesHelper {
    private ApplicationManager app;

    //создается telnet клиент
    private TelnetClient telnet;
    private InputStream in;
    private PrintStream out;

    private Session mailSession;
    private Store store;
    private String mailServer;

    public JamesHelper(ApplicationManager app) {
        this.app = app;
        telnet = new TelnetClient();
        //создается почтовая сессия
        mailSession = Session.getDefaultInstance(System.getProperties());
    }

    //проверка существования юзера
    public boolean doesUserExist(String name) {
        initTelnetSession();
        write("veryfy" + name);
        String result = readUntil("exist");
        closeTelnetSession();
        return result.trim().equals("User " + name + "exist");
    }

    //для создания пользователя
    public void createUser(String name, String password) {
        //устанавливается соединение по протоколу Telnet
        initTelnetSession();
        //пишем команду в консоль
        write("adduser" + name + " " + password);
        //ждем появления на консоли текста что юзер добавлен
        String result = readUntil("User " + name + " added");
        //закрытие соединения
        closeTelnetSession();
    }

    //для удаления пользователя
    public void deleteUser(String name) {
        initTelnetSession();
        write("deluser" + name);
        String result = readUntil("User " + name + " deleted");
        closeTelnetSession();
    }

    private void initTelnetSession() {
        //для установления соединение c внешним сервером получить свойства из конф-го файла
        mailServer = app.getProperty("mailserver.host");
        //mailServer = (String) app.getProperty("mailserver.host");
        int port = Integer.parseInt((String) app.getProperty("mailserver.port"));
        //String login = (String) app.getProperty("mailserver.adminlogin");
        String login = app.getProperty("mailserver.adminlogin");
        //String password = (String) app.getProperty("mailserver.adminpassword");
        String password = app.getProperty("mailserver.adminpassword");

        try {
            //устанавливаем сединение с почт.сервером
            telnet.connect(mailServer, port);
            //берем входной поток (чтобы что то читать - даные которые telnet клиент отпраляет)
            in = telnet.getInputStream();
            // и выходной поток (чтобы писать ему команды)
            out = new PrintStream(telnet.getOutputStream() );
        } catch (Exception e) {
            e.printStackTrace();
        }

        //схема взаимодействия - пишем, ждем, пишем, ждем
        readUntil("Login id:");
        write("");
        readUntil("Password:");
        write("");

        readUntil("Login id:");
        write(login);
        readUntil("Password:");
        write(password);

        //после успешного входа проверяем сообщение
        readUntil("Welcome " + login + ". HELP for a list of commands");
    }

    //чтение данных и сравнение с заданным шаблоном
    private String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                System.out.println(ch);
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //запись данных
    public void write(String value) {
        try {
            out.println(value);
            out.flush();
            System.out.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //выход
    public void closeTelnetSession() {
        write("quit");
    }

    /*
    //очистка почт.ящика
    public void drainEmail(String username, String рassword) throws МеssagingException {
        Folder inbox = openInbox(username, password);
        for (Меssаge mеssage : inbox.getМеssages()) {
        //каждое сообщение помечается DELETED
            message.setFlag(Flags.Flag.DELETED, true);
            closeFolder(inbox);

     */

    private void closeFolder(Folder folder) throws MessagingException {
        //закрыть папку INBOX, удалить все пиьма помеченные к удалению
        folder.close(true);
        store.close();
    }

    //открытие почт.ящика
    private Folder openInbox(String username, String password) throws MessagingException {
        //берем почт.сессию с указываем что работаем по протоколу pop3
        store = mailSession.getStore("pop3");
        //устанавливаем соединение, указываем адрес почт.сервера, имя пользователя и пароль
        store.connect(mailServer, username, password);
        //доступ к папке inbox
        Folder folder = store.getDefaultFolder().getFolder("INBOX");
        //открываем папку INBOX на чтение/запись
        folder.open(Folder.READ_WRITE);
        //возвращаем открытую папку
        return folder;
    }

    //ждем почту
    public List<MailMessage> waitForMail(String usermane, String password, long timeout) throws
            MessagingException {
        //момент начала ожидания
        long now = System.currentTimeMillis();
        //проверяем что текущее время не превышает время старта + заданный таймаут
        while (System.currentTimeMillis() < now + timeout) {
            //получаем всю почту
            List<MailMessage> allMail = getAllMail(usermane, password);
            //если есть хотя бы 1 письмо возвращаем список письм
            if (allMail.size() > 0) {
                return allMail;
            }
            //если почты нет ждем 1 сек и проверяем снова ящик
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        throw new Error("No mail :(");
    }

    //метод извлекает сообщения из почтового ящика и превращает их в модельные объекты
    public List<MailMessage> getAllMail(String username, String password) throws MessagingException {
        //открыть почтовый ящик
        Folder inbox = openInbox(username, password);
        //берем список писем, превращаем в поток, применяем ф-ию toModelMail(m) которая превращает их в модельные объекты
        //и собираем поток обратно в список
        List<MailMessage> messages = Arrays.asList(inbox.getMessages()).stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
        //закрыть почтовый ящик
        closeFolder(inbox);
        //и возвращаем список
        return messages;
    }

    //преобразование реальных письем в модельные объекты
    public static MailMessage toModelMail(Message m) {
        try {
            //получаем список адресов, берем 1ый адрес, преобразуем в строку и строим модельный объект
            return new MailMessage(m.getAllRecipients()[0].toString(), (String) m.getContent());
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
