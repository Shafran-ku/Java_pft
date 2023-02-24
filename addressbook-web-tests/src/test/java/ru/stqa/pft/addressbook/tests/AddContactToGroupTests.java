package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

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
    }

    @Test
    public void testAddContactToGroup() {
        //выбор контакта
        Contacts contact = app.db().contacts();
        ContactData selectedContact = contact.iterator().next();

        //выбор группы
        GroupData selectedGroup;
        Groups groups = app.db().groups();

        //если контакт уже есть в выбранной группе то создать новую, иначе использовать существующую
        if (groups.size() == selectedContact.getGroups().size()) {
            GroupData newGroup = new GroupData().withName("test 2");
            selectedGroup = newGroup;
            app.goTo().groupPage();
            app.group().create(newGroup);

        } else {
            selectedGroup = groups.iterator().next();
        }

        app.goTo().HomePage();

        //добавить контакт в выбранную группу
        app.contact().initAdditionToGroup(selectedContact, selectedGroup);

        app.goTo().HomePage();

        //проверить что контакт в группе
        app.contact().checkContactInGroup(selectedGroup, selectedContact);

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyContactListInUI();
    }


}