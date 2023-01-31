package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

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
    }

    @Test
    public void testAddContactToGroup() {
        //до добавления контакта из группы
        Contacts before = app.db().contacts();

        //данные контакта для добавления
        ContactData selectedContact = before.iterator().next();

        //кол-во групп
        GroupData selectedGroup;
        Groups groups = app.db().groups();

        //проверка если контакт уже есть в существующей группе(ах) то создать новую, иначе использовать существующую
        if (groups.size() == selectedContact.getGroups().size()) {
            GroupData newGroup = new GroupData().withName("test 2");
            selectedGroup = newGroup;
            app.goTo().groupPage();
            app.group().create(newGroup);

        } else selectedGroup = groups.iterator().next();

        app.goTo().HomePage();

        //добавить контакт в созданную группу
        app.contact().initAdditionToGroup(selectedContact, selectedGroup);

        app.goTo().HomePage();

        //проверить что контакт в группе
        app.contact().checkContactInGroup(selectedGroup);
        Contacts after = app.db().contacts();

        assertThat(after, equalTo(before));

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyContactListInUI();
    }


}