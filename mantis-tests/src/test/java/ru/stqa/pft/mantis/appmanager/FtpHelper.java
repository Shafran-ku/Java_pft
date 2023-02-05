package ru.stqa.pft.mantis.appmanager;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FtpHelper {
    private final ApplicationManager app;
    private FTPClient ftp;

    //при вызове конструктора выполняется инициализация, создается ftp клиент
    public FtpHelper(ApplicationManager app) {
        this.app = app;
        ftp = new FTPClient();
    }

    //метод загружает новый конфиг, старый переименовывает
    //метод принимает 3 параметра: 1-файл, который дб загружен на удаленную машину, 2-имя файла, куда загружается,
    //3-имя резервной копии, если удаленный файл уже существует
    public void upload(File file, String target, String backup) throws IOException {
        //устанавливаем соединение с сервером
        ftp.connect(app.getProperty("ftp.host"));
        //выполняем логин
        ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
        //удаляем предыдущую резервную копию
        ftp.deleteFile(backup);
        //переименовываем удяляемый файл (делаем резервную копию)
        ftp.rename(target, backup);
        //включаем пассивный режим передачи данных (ограничения используемого ftp-сервера)
        ftp.enterLocalPassiveMode();
        //передаем файл; FileInputStream - предназначен для чтения бинарных файлов(чтение происходит побайтово)
        ftp.storeFile(target, new FileInputStream(file));
        //разрыв соединения после передачи
        ftp.disconnect();
    }

    //метод восстанавливает старый файл
    public void restore(String backup, String target) throws IOException {
        //соединение с удал.машиной по ftp
        ftp.connect(app.getProperty("ftp.host"));
        //логин
        ftp.login(app.getProperty("ftp.login"), app.getProperty("ftp.password"));
        //удаляем предыдущую резервную копию
        ftp.deleteFile(target);
        //переименовываем удяляемый файл (делаем резервную копию)
        ftp.rename(backup, target);
        //разрыв соединения после передачи
        ftp.disconnect();
    }
}