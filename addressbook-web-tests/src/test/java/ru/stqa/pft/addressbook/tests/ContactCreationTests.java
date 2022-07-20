package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

public class ContactCreationTests extends TestBase {
    @Test
    public void testContactCreation() throws Exception {
        //проверка наличия группы test1 при создании контакта, если такой группы нет, то создать ее
        if (! app.getContactHelper().isAnyGroupExist()) {
            app.getGroupHelper().createGroup(new GroupData("test1", null, null));
        }

        app.getContactHelper().createContact(new ContactData("Den", "Kh.", "Suvorova st.",
                "den@mail.ru", "+79188888777", "test1"));
    }

}
