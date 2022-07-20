package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase {
    @Test
    public void testContactCreation() throws Exception {

        app.getContactHelper().createContact(new ContactData("Den", "Kh.", "Suvorova st.",
                "den@mail.ru", "+79188888777", "test1"));
    }

}
