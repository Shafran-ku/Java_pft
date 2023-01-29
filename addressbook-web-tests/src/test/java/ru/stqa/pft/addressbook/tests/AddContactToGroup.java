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
        if (app.db().groups().size()== 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("test 1"));
        }

        //если нет ни одного контакта то создать
        if (app.db().contacts().size() == 0) {
            app.goTo().HomePage();
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh."));
        }
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