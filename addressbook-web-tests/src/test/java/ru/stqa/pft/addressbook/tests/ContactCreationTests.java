package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.HashSet;
import java.util.List;

public class ContactCreationTests extends TestBase {
    @Test
    public void testContactCreation() throws Exception {

        //список элементов до добавления
        List<ContactData> before = app.getContactHelper().getContactList();

        app.getNavigationHelper().gotoGroupPage();
        //проверка наличия хоть одной группы при создании контакта, если ни одной группы нет, то создать
        if (! app.getGroupHelper().isAnyGroupExist()) {
            app.getGroupHelper().createGroup(new GroupData("test1", null, null));
        }
        app.getNavigationHelper().goToHomePage();

        //сделали переменную
        ContactData contact = new ContactData("Den", "Kh.", "Suvorova st.",
                "den@mail.ru", "+79188888777", "test1");
        app.getContactHelper().createContact(contact);

        //список элементов после того как будет создана новая группа
        List<ContactData> after = app.getContactHelper().getContactList();

        //сравнение размера списков до и после добавления
        Assert.assertEquals(after.size(), before.size() + 1);

        //ищем добавленный контакт (у него будет максимальный id)
        int max = 0;
        for (ContactData c : after) {
            if (c.getId() > max) {
                max = c.getId();
            }
        }
        contact.setId(max);
        before.add(contact);
        Assert.assertEquals(new HashSet<Object>(before), new HashSet<Object>(after));
    }

}
