package ru.stqa.pft.addressbook.generators;

import ru.stqa.pft.addressbook.model.GroupData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class GroupDataGenerator {
    //в качестве параметра принимает массив строк
    public static void main(String[] args) throws IOException {
        //генерация данных
        //передается количество строк и путь к файлу
        int count = Integer.parseInt(args[0]);
        File file = new File(args[1]);

        //сохранение данных в файл
        List<GroupData> groups = generateGroups(count);
        save(groups, file);
    }

    //запись в файл для всех групп (предусмотрели исключение (IOException))
    private static void save(List<GroupData> groups, File file) throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        //открыть файл на запись
        Writer writer = new FileWriter(file);
        for (GroupData group : groups) {
            writer.write(String.format("%s;%s;%s\n", group.getName(), group.getHeader(), group.getFooter()));
        }
        //закрытие файла
        writer.close();
    }

    private static List<GroupData> generateGroups(int count) {
        //создаем новый список объектов типа GroupData
        List<GroupData> groups = new ArrayList<GroupData>();
        for (int i = 0; i < count; i++) {
            groups.add(new GroupData().withName(String.format("test %s", i))
                    .withHeader(String.format("header %s", i)).withFooter(String.format("footer %s", i)));
        }
        return groups;
    }
}
