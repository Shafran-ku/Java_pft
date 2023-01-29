package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AddContactToGroup extends TestBase {

    //предусловия теста
    @BeforeMethod
    public void ensurePreconditions() {
        //если нет ни одной группы то создать

        //если нет ни одного контакта то создать
    }

    @Test
    public void testAddContactToGroup() {
        Contacts before = app.db().contacts();
        app.goTo().HomePage();

        //выбор контакта
        ContactData selectContact = before.iterator().next();
        ContactData contact = new ContactData().withId(selectContact.getId());

        //выбор группы
        GroupData group = new GroupData().withName("test 1");
        app.contact().initAdditionToGroup(contact, group);

        app.goTo().HomePage();

        app.contact().checkAdded(group);
        Contacts after = app.db().contacts();

        assertThat(after, equalTo(before));

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyContactListInUI();
    }


}