package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Comparator;
import java.util.List;

public class ContactCreationTests extends TestBase {
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

    @Test
    public void testContactCreation() throws Exception {

        //список элементов до добавления
        List<ContactData> before = app.contact().list();

        //сделали переменную
        ContactData contact = new ContactData("Den", "Kh.", "Suvorova st.",
                "den@mail.ru", "+79188888777", "test1");
        app.contact().create(contact);

        //список элементов после того как будет создана новая группа
        List<ContactData> after = app.contact().list();

        //сравнение размера списков до и после добавления
        Assert.assertEquals(after.size(), before.size() + 1);

        before.add(contact);

        //локальная переменная + лямбда  выражение(анонимая ф-я, на вход принимает 2 сравниваемых контакта, и выполняет сравнение их идент-ов
        Comparator<? super ContactData> byId = (c1, c2) -> Integer.compare((c1.getId()), c2.getId());

        //сортируем старый список
        before.sort(byId);
        //сортируем новый список
        after.sort(byId);

        //сравниваем упорядоченные списки (контакты сравниваеются по firstname и lastname, id не учитываются
        Assert.assertEquals(before, after);
    }

}
