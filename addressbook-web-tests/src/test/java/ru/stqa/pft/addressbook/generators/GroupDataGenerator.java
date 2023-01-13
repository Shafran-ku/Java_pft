package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class GroupDataGenerator {
    //опции командной строки
    @Parameter(names = "-c", description = "Group count")
    public int count;

    @Parameter(names = "-f", description = "Target file")
    public String file;

    @Parameter(names = "-d", description = "Data format")
    public String format ;

    //в качестве параметра принимает массив строк
    public static void main(String[] args) throws IOException {
        //создаем объект текущего класса
        GroupDataGenerator generator = new GroupDataGenerator();

        //создаем объект типа jcommander и помещаем его в локальную переменную
        JCommander jCommander = new JCommander(generator);

        //используем try-catch, перехватываем параметр Exception
        //и если исключение возникло, то выводим с помощью метода usage на консоль инф-ю о доступных опциях
        try {
            jCommander.parse(args);
        } catch (ParameterException ex) {
            jCommander.usage();
            return;
        }

        //запуск
        generator.run();

     }

    private void run() throws IOException {
        //сохранение данных в файл
        List<GroupData> groups = generateGroups(count);

        //если формат csv то сохраняем csv, иначе xml
        if (format.equals("csv")) {
            saveAsCsv(groups, new File(file));
        } else if (format.equals("xml")) {
            saveAsXml(groups, new File(file));
        } else {
            System.out.println("Unrecognised format " + format);
        }

    }

    //сохранение в формате xml с помощью библиотеки XStream
    private void saveAsXml(List<GroupData> groups, File file) throws IOException {
        XStream xstream = new XStream();

        //сохраняем с нужным именем
        //для класса GroupData прочитать подсказки, которые в нем написаны (аннотация)
        xstream.processAnnotations(GroupData.class);

        //указать объект, который хотим преобразовать в xml
        String xml = xstream.toXML(groups);

        Writer writer = new FileWriter(file);
        writer.write(xml);
        writer.close();

    }

    //запись в файл для всех групп (предусмотрели исключение (IOException))
    private void saveAsCsv(List<GroupData> groups, File file) throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        //открыть файл на запись
        Writer writer = new FileWriter(file);
        for (GroupData group : groups) {
            writer.write(String.format("%s;%s;%s\n", group.getName(), group.getHeader(), group.getFooter()));
        }
        //закрытие файла
        writer.close();
    }

    private List<GroupData> generateGroups(int count) {
        //создаем новый список объектов типа GroupData
        List<GroupData> groups = new ArrayList<GroupData>();
        for (int i = 0; i < count; i++) {
            groups.add(new GroupData().withName(String.format("test %s", i))
                    .withHeader(String.format("header %s", i)).withFooter(String.format("footer %s", i)));
        }
        return groups;
    }
}
