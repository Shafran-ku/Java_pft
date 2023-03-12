package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AddContactToGroupTests extends TestBase {

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

        app.goTo().HomePage();

        //проверяем есть ли контакты без групп, если нет - создаем
        app.contact().showContactsWithoutGroup();
        Set<ContactData> contactsWithoutGroup = app.contact().all();
        if (contactsWithoutGroup.size() == 0) {
            app.goTo().HomePage();
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh."));
        }
    }

    @Test
    public void testAddContactToGroup() {
        app.goTo().HomePage();

        //найти контакт без групп
        app.contact().showContactsWithoutGroup();
        Set<ContactData> contactsWithoutGroup = app.contact().all();
        ContactData contactNoneGroup = contactsWithoutGroup.iterator().next();
        int id = contactNoneGroup.getId();
        ContactData contact = app.db().getContactById(id);

        //добавляем контакт в группу
        GroupData group = app.db().groups().iterator().next();
        app.contact().addContactToGroup(contact, group);

        //проверяем что контакт в группе
        assertThat(app.db().getContactById(contact.getId()).getGroups().contains(group), equalTo(true));

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyContactListInUI();
    }
}