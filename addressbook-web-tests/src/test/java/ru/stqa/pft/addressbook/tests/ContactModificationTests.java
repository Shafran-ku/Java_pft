package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;

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

        //список элементов до модификации
        List<ContactData> before = app.getContactHelper().getContactList();

        app.getContactHelper().selectAndInitContactModification(before.size() - 1);
        app.getContactHelper().fillContactForm(new ContactData("Den", "Kh.", "Suvorova st.",
                "den@mail.ru", "+79188888777", null), false);
        app.getContactHelper().submitContactCreation();
        app.getNavigationHelper().goToHomePage();

        //список элементов после того как будет модификация
        List<ContactData> after = app.getContactHelper().getContactList();


        //сравнение кол-ва контактов до добавления и после
        Assert.assertEquals(after.size(), before.size());
    }

}
