package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

public class ContactModificationTests extends TestBase {
    @Test
    public void testContactModification() {
        app.getNavigationHelper().goToHomePage();
        if (!app.getContactHelper().isThereAnyContact()) {
            app.getNavigationHelper().gotoGroupPage();
            if (!app.getGroupHelper().isAnyGroupExist()) {
                app.getGroupHelper().createGroup(new GroupData("test1", null, null));
            }
            app.getContactHelper().createContact(new ContactData("Den", "Kh.", "Suvorova st.",
                    "den@mail.ru", "+79188888777", "test1"));
        }
        app.getContactHelper().selectAndInitContactModification();
        app.getContactHelper().fillContactForm(new ContactData("Den", "Kh.", "Suvorova st.",
                "den@mail.ru", "+79188888777", null), false);
        app.getContactHelper().submitContactCreation();
    }

}
