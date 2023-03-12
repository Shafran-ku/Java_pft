
package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
            Groups groups = app.db().groups();
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh.")
                    .inGroup(groups.iterator().next()));
        }
    }

    @Test
    public void testDeleteContactFromGroup() {

        ContactData contact = app.db().contacts().iterator().next();
        int id = contact.getId();

        if(contact.getGroups().size() == 0){
            GroupData group = app.db().groups().iterator().next();
            app.goTo().HomePage();
            app.contact().addContactToGroup(contact, group);
            app.goTo().HomePage();
        }

        ContactData new_contact = app.db().getContactById(id);
        Groups groupDelete = new_contact.getGroups();
        app.contact().removeContactFromGroup(new_contact);

        //проверить что контакт удален из группы
        assertThat(app.db().getContactById(contact.getId()).getGroups().contains(groupDelete), equalTo(false));
    }
}
