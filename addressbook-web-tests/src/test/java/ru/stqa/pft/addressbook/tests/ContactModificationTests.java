package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase {
    @Test
    public void testContactModification() {
        app.getContactHelper().goToHomePage();
        app.getContactHelper().selectAndInitContactModification();
        app.getContactHelper().fillContactForm(new ContactData("Den", "Kh.", "Suvorova st.",
                "den@mail.ru", "+79188888777", null), false);
        app.getContactHelper().submitContactCreation();
    }

}
