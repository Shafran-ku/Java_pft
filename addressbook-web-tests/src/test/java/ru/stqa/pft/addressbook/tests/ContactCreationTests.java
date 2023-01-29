package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import org.openqa.selenium.json.TypeToken;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {

    @DataProvider //для JSON
    public Iterator<Object[]> validContactsFromJson() throws IOException {
        //ридер для чтения данных c конструкцией try - (инициализация) {использование}
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contacts.json")))) {
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
            //TypeToken<List<ContactData>>(){}.getType() - для обработки типа данных, заключенных в <> (<ContactData>)
            List<ContactData> groups = gson.fromJson(json, new TypeToken<List<ContactData>>(){}.getType());

            //к каждому объекту нужно применить функцию, которая этот объект завернет в массив
            //collectors из потока собирает список
            //и у этого cписка берем итератор, который возвращаем
            return groups.stream().map((g) -> new Object[] {g}).collect(Collectors.toList()).iterator();
        }
    }

    //предусловия теста
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().groupPage();
        //проверка наличия хоть одной группы при создании контакта, если ни одной группы нет, то создать
        if (! app.group().isAnyGroupExist()) {
            app.group().create(new GroupData().withName("test1"));
        }
        app.goTo().HomePage();
    }


    @Test(dataProvider = "validContactsFromJson")
    public void testContactCreation(ContactData contact) {
        //Основной тест

        //получаем все группы
        Groups groups = app.db().groups();

        //множество элементов до добавления
        Contacts before = app.db().contacts();

        //переменная для картинки
        File photo= new File("src/test/resources/stru.png");

        /*
        //старое создание контакта с фото
        ContactData contact = new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                .withEmail("den@mail.ru").withHomePhone("+79188888777").withGroup("test1").withPhoto(photo);
         */

        app.contact().create(contact);

        //сравнение размера до и после добавления
        assertThat(app.contact().count(), equalTo(before.size() + 1));

        //множество элементов после того как будет создана новая группа
        Contacts after = app.db().contacts();

        //сравниваем по содержимому (контакты сравниваеются по firstname и lastname, id не учитываются
        assertThat(after, equalTo(before.withAdded(
                contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyContactListInUI();
    }

    @Test(enabled = false)
    public void testBadContactCreation() throws Exception {

        //множество элементов до добавления
        Contacts before = app.contact().all();  //Todo Contacts before = app.db().contacts();

        //сделали переменную
        //ContactData contact = new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
        //        .withEmail("den@mail.ru").withHomePhone("+79188888777").withGroup("test1");
        //app.contact().create(contact);

        //сравнение размера до и после добавления
        assertThat(app.contact().count(), equalTo(before.size()));

        //множество элементов после того как будет создана новая группа
        Contacts after = app.contact().all();   //todo Contacts after = app.db().contacts();

        //сравниваем по содержимому (контакты сравниваеются по firstname и lastname, id не учитываются
        assertThat(after, equalTo(before));
    }

}
