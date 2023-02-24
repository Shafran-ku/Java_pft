
package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

public class DeleteContactFromGroupTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        //если нет ни одной группы то создать
        if (app.db().groups().size() == 0) {
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
    public void testDeleteContactFromGroup() {
        //выбор контакта
        Contacts contacts = app.db().contacts();
        ContactData selectedContact = contacts.iterator().next();

        //выбор группы
        Groups groups = app.db().groups();
        GroupData selectedGroup = groups.iterator().next();

        app.goTo().HomePage();
        //выбрать группу
        app.contact().selectGroup(selectedGroup);
        //если выбранного контакта нет в выбраной группе, то добавить
        if (app.db().contacts().size() == 0 ||
                !app.contact().isElementPresent(By.cssSelector("input[value = '" + selectedContact.getId() + "']"))) {
            //переключить в [All]
            app.contact().checkAllPage();
            app.contact().selectContactById(selectedContact.getId());
            //добавить выбранный контакт в выбранную группу
            app.contact().addToGroup(selectedGroup);
            app.goTo().HomePage();
            app.contact().selectGroup(selectedGroup);
        }

        //удаляем контакт из группы
        app.contact().deleteContactFromGroup(selectedContact);
        app.goTo().HomePage();

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyContactListInUI();
    }
}
