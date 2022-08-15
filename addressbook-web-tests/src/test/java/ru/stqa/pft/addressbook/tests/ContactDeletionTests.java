package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.List;

public class ContactDeletionTests extends TestBase {
    @Test(enabled = false)
    public void testContactDeletion() {
        app.getNavigationHelper().goToHomePage();

       //проверка наличия контакта для удаления: если нечего удалять, то создать контакт
        if (! app.getContactHelper().isThereAnyContact()) {
            app.getContactHelper().createContact(new ContactData("Den", "Kh.", "Suvorova st.",
                    "den@mail.ru", "+79188888777", "test1"));
        }
        //список элементов до удаления
        List<ContactData> before = app.getContactHelper().getContactList();

        app.getContactHelper().selectAndInitContactModification(before.size() - 1);
        app.getContactHelper().deleteSelectedContact();
        app.getNavigationHelper().goToHomePage();

        //список элементов после удаления контакта
        List<ContactData> after = app.getContactHelper().getContactList();

        //сравнение кол-ва контактов до добавления и после
        Assert.assertEquals(after.size(), before.size() - 1);

        
        //удаляем ненужный элемент из списка для сравнения
        before.remove(before.size() - 1);

        //сравниваем 2 списка
        Assert.assertEquals(before, after);
        }
    }

