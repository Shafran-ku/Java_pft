package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Set;

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

        //множество элементов до добавления
        Set<ContactData> before = app.contact().all();

        //сделали переменную
        ContactData contact = new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                .withEmail("den@mail.ru").withHomephone("+79188888777").withGroup("test1");
        app.contact().create(contact);

        //множество элементов после того как будет создана новая группа
        Set<ContactData> after = app.contact().all();

        //сравнение размера списков до и после добавления
        Assert.assertEquals(after.size(), before.size() + 1);


        //поток объектов типа contactData превратим в поток идент-ров
        //mapToInt - анонимная ф-ия, которая будет примняться ко всем элементам потока и каждый из них будет преобраз-ся в число
        //анонимная ф-ия в качестве параметра принимает контакт, а в качестве рез-та выдает идент-ор контакта (т.е. преобразует объект в число)
        //max() - находим максимальный и получаем число (getAsInt)
        contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt());
        before.add(contact);

        //сравниваем (контакты сравниваеются по firstname и lastname, id не учитываются
        Assert.assertEquals(before, after);
    }

}
