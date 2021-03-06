package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

public class ContactCreationTests extends TestBase {
    @Test
    public void testContactCreation() throws Exception {
        //кол-во контактов до добавления
        int before = app.getContactHelper().getContactCount();

        app.getNavigationHelper().gotoGroupPage();
        //проверка наличия хоть одной группы при создании контакта, если ни одной группы нет, то создать
        if (! app.getGroupHelper().isAnyGroupExist()) {
            app.getGroupHelper().createGroup(new GroupData("test1", null, null));
        }
        app.getNavigationHelper().goToHomePage();
        app.getContactHelper().createContact(new ContactData("Den", "Kh.", "Suvorova st.",
                "den@mail.ru", "+79188888777", "test1"));

        //кол-во контактов после добавления
        int after = app.getContactHelper().getContactCount();
        //сравнение кол-ва контактов до добавления и после
        Assert.assertEquals(after, before + 1);
    }

}
