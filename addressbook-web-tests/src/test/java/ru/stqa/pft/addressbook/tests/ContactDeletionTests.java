package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactDeletionTests extends TestBase {
    @Test
    public void testContactDeletion() {
        app.getNavigationHelper().goToHomePage();

        //кол-во контактов до добавления
        int before = app.getContactHelper().getContactCount();

        //проверка наличия контакта для удаления: если нечего удалять, то создать контакт
        if (! app.getContactHelper().isThereAnyContact()) {
            app.getContactHelper().createContact(new ContactData("Den", "Kh.", "Suvorova st.",
                    "den@mail.ru", "+79188888777", "test1"));
        }
        app.getContactHelper().selectAndInitContactModification();
        app.getContactHelper().deleteSelectedContact();
        app.getNavigationHelper().goToHomePage();

        //кол-во контактов после добавления
        int after = app.getContactHelper().getContactCount();

        //сравнение кол-ва контактов до добавления и после
        Assert.assertEquals(after, before - 1);
    }
}
