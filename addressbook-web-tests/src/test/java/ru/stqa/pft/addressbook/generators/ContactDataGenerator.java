package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.stqa.pft.addressbook.model.ContactData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

//перед запуском теста проверить строку параметров в конфигах GroupDataGenerator (-f src/test/resources/contacts.json -c 3 -d json)

public class ContactDataGenerator {

    //опции командной строки
    @Parameter(names = "-c", description = "Contact count")
    public int count;

    @Parameter(names = "-f", description = "Target file")
    public String file;

    @Parameter(names = "-d", description = "Data format")
    public String format;

    //в качестве параметра принимает массив строк
    public static void main(String[] args) throws IOException {
        //создаем объект текущего класса
        ContactDataGenerator generator = new ContactDataGenerator();

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
        List<ContactData> contacts = generateContacts(count);

        //если формат json то сохраняем json, иначе предупреждение
        if (format.equals("json")) {
            saveAsJson(contacts, new File(file));
        } else {
            System.out.println("Unrecognised format " + format);
        }
    }

    private List<ContactData> generateContacts(int count) {
        //создаем новый список объектов типа ContactData
        List<ContactData> contacts = new ArrayList<ContactData>();
        for (int i = 0; i < count; i++) {
            contacts.add(new ContactData().withFirstname(String.format("firstname %s", i))
                    .withLastname(String.format("lastname %s", i))
                    .withAddress(String.format("address %s", i))
                    .withEmail(String.format("email %s", i))
                    .withHomePhone(String.format("%s%s%s-%s%s%s", i,i,i,i,i,i)));
        }
        return contacts;
    }

    private void saveAsJson(List<ContactData> contacts, File file) throws IOException {
        //setPrettyPrinting позволяет выводить красивый (отформатированный) json
        //excludeFieldsWithoutExposeAnnotation() - пропускать все поля, которые не помечены аннотацией @Expose
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(contacts);

        //инициализация + закрытие файла
        try(Writer writer = new FileWriter(file)){
            writer.write(json);
        }
    }
}

