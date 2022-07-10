package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase {
    @Test
    public void testContactCreation() throws Exception {
        app.initContactCreation();
        app.fillContactForm(new ContactData("Den", "Kh.", "Suvorova st.", "den@mail.ru", "+79188888777"));
        app.submitContactCreation();
        app.returnToHomePage();
    }

}
