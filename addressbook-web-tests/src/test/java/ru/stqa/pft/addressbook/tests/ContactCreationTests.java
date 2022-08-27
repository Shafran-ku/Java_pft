package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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
        Contacts before = app.contact().all();

        //сделали переменную
        ContactData contact = new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                .withEmail("den@mail.ru").withHomePhone("+79188888777").withGroup("test1");
        app.contact().create(contact);

        //сравнение размера до и после добавления
        assertThat(app.contact().count(), equalTo(before.size() + 1));

        //множество элементов после того как будет создана новая группа
        Contacts after = app.contact().all();

        //сравниваем по содержимому (контакты сравниваеются по firstname и lastname, id не учитываются
        assertThat(after, equalTo(before.withAdded(
                contact.withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));
    }

    @Test
    public void testBadContactCreation() throws Exception {

        //множество элементов до добавления
        Contacts before = app.contact().all();

        //сделали переменную
        ContactData contact = new ContactData().withFirstname("Den  '  ").withLastname("Kh.").withAddress("Suvorova st.")
                .withEmail("den@mail.ru").withHomePhone("+79188888777").withGroup("test1");
        app.contact().create(contact);

        //сравнение размера до и после добавления
        assertThat(app.contact().count(), equalTo(before.size()));

        //множество элементов после того как будет создана новая группа
        Contacts after = app.contact().all();

        //сравниваем по содержимому (контакты сравниваеются по firstname и lastname, id не учитываются
        assertThat(after, equalTo(before));
    }

}
