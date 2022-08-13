package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class ContactModificationTests extends TestBase {
    @Test
    public void testContactModification() {
        app.getNavigationHelper().goToHomePage();

        //проверка есть ли контакт, если нет то проверка наличия группы
        if (!app.getContactHelper().isThereAnyContact()) {
            app.getNavigationHelper().gotoGroupPage();
            //проверка есть ли хоть одна группа, если нет - создаем
            if (!app.getGroupHelper().isAnyGroupExist()) {
                app.getGroupHelper().createGroup(new GroupData("test1", null, null));
            }
            //если нет контакта, но есть группа, создаем контакт
            app.getContactHelper().createContact(new ContactData("Den", "Kh.", "Suvorova st.",
                    "den@mail.ru", "+79188888777", "test1"));
        }

        //список элементов до модификации
        List<ContactData> before = app.getContactHelper().getContactList();

        //выбираем последний элемент в списке контактов для модификации
        app.getContactHelper().selectAndInitContactModification(before.size() - 1);

        //сделали переменную, чтобы не писать одно и то же
        //при модификации контакта указываем новые данные, а идентификатор сохраняем старый (before.get(before.size() - 1).getId())
        ContactData contact =  new ContactData(before.get(before.size() - 1).getId(),"Den", "Kh.", "Suvorova st.",
                "den@mail.ru", "+79188888777", null);

        app.getContactHelper().fillContactForm(contact, false);
        app.getContactHelper().submitContactModification();
        //app.getContactHelper().submitContactCreation();
        app.getNavigationHelper().goToHomePage();

        //список элементов после того как будет модификация
        List<ContactData> after = app.getContactHelper().getContactList();

        //сравнение кол-ва контактов до добавления и после
        Assert.assertEquals(after.size(), before.size());

        //модифицируем старый список - удалим из списка элемент, который ранее удаляли
        before.remove(before.size() - 1);
        //а вместо удаленного добавим элемент, который должен появиться после модификации
        before.add(contact);

        //сортируем списки до и после, затем сравниваем:

        //локальная переменная + лямбда  выражение(анонимая ф-я, на вход принимает 2 сравниваемых контакта, и выполняет сравнение их идент-ов
        Comparator<? super ContactData> byId = (c1, c2) -> Integer.compare((c1.getId()), c2.getId());
        //сортируем первый список
        before.sort(byId);
        //сортируем второй список
        after.sort(byId);

        Assert.assertEquals(before, after);
    }

}
