package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import org.openqa.selenium.json.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

//перед запуском теста проверить строку параметров в конфигах GroupDataGenerator (-f src/test/resources/groups.xml -c 3 -d xml)

public class GroupCreationTests extends TestBase {

    //создаем логгер и указываем класс GroupCreationTests, с которым логгер будет ассоциирован
    Logger logger = LoggerFactory.getLogger(GroupCreationTests.class);

    @DataProvider //для XML
    public Iterator<Object[]> validGroupsFromXml() throws IOException {
        //ридер для чтения данных c конструкцией try - (инициализация) {использование}
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.xml")))) {

            //десериализация формата xml при чтении
            String xml = "";

            //читаем строки
            String line = reader.readLine();

            //для чтения всех строк файла делаем цикл
            while (line != null) {
                xml += line;
                line = reader.readLine();
            }
            //новый объект XStream
            XStream xstream = new XStream();

            //xstream должен обработать аннотации
            xstream.processAnnotations(GroupData.class);

            //доработка инициализации xstream из-за новых ограничений безопасности, которые появились недавно
            xstream.allowTypes(new Class[]{GroupData.class});

            //должен прочитать данные типа List<GroupData> и сохранить в переменную того же типа
            List<GroupData> groups = (List<GroupData>) xstream.fromXML(xml);

            //к каждому объекту нужно применить функцию, которая этот объект завернет в массив
            //collectors из потока собирает список
            //и у этого cписка берем итератор, который возвращаем
            return groups.stream().map((g) -> new Object[] {g}).collect(Collectors.toList()).iterator();
        }
    }

    @DataProvider //для JSON
    public Iterator<Object[]> validGroupsFromJson() throws IOException {
        //ридер для чтения данных c конструкцией try - (инициализация) {использование}
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/groups.json")))) {
            //десериализация формата json: читаем содержимое файла в переменную, потом ее обрабатываем
            String json = "";

            //читаем строки
            String line = reader.readLine();

            //для чтения всех строк файла делаем цикл
            while (line != null) {
                json += line;
                line = reader.readLine();
            }
            //новый объект
            Gson gson = new Gson();
            //TypeToken<List<GroupData>>(){}.getType() - для обработки типа данных, заключенных в <> (<GroupData>)
            List<GroupData> groups = gson.fromJson(json, new TypeToken<List<GroupData>>(){}.getType());

            //к каждому объекту нужно применить функцию, которая этот объект завернет в массив
            //collectors из потока собирает список
            //и у этого cписка берем итератор, который возвращаем
            return groups.stream().map((g) -> new Object[] {g}).collect(Collectors.toList()).iterator();
        }
    }

    //указали параметр dataProvider, кол-во принимаемых параметров = кол-ву параметров в тестовом наборе
    @Test(dataProvider = "validGroupsFromJson")
    public void testGroupCreation( GroupData group) {

        //основной тест
        app.goTo().groupPage();
        Groups before = app.db().groups();
        app.group().create(group);

        //сравниваем размеры списков
        assertThat(app.group().count(), equalTo(before.size() + 1));

        Groups after = app.db().groups();

        //анонимная ф-ия в качестве параметра принимает группу, а в качестве рез-та выдает идент-ор группы
        assertThat(after, equalTo(
                before.withAdded(group.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyGroupListInUI();
    }

    @Test(enabled = false)
    public void testBadGroupCreation() throws Exception {
        app.goTo().groupPage();
        Groups before = app.db().groups();
        GroupData group = new GroupData().withName("test2   ' ");
        app.group().create(group);

        //сравниваем размеры списков
        assertThat(app.group().count(), equalTo(before.size()));
        Groups after = app.db().groups();
        assertThat(after, equalTo(before));
    }

}

