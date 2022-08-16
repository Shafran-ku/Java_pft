package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Comparator;
import java.util.List;

public class ContactModificationTests extends TestBase {
   //предусловия теста
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();

        //проверка есть ли контакт, если нет то проверка наличия группы
        if (app.contact().list().size() == 0) {
            app.goTo().groupPage();
            //проверка есть ли хоть одна группа, если нет - создаем
            if (app.group().list().size() == 0) {
                app.group().create(new GroupData().withName("test1"));
            }
            //если нет контакта, но есть группа, создаем контакт
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                    .withEmail("den@mail.ru").withHomephone("+79188888777").withGroup("test1"));
        }
    }

    @Test
    public void testContactModification() {

        //список элементов до модификации
        List<ContactData> before = app.contact().list();

        //индекс контакта, который мы модифицируем
        int index = before.size() - 1;

        //при модификации контакта указываем новые данные, а идентификатор сохраняем старый (before.get(before.size() - 1).getId())
        ContactData contact =  new ContactData().withId(before.get(index).getId()).withFirstname("Den")
                .withLastname("Kh.").withAddress("Suvorova st.").withEmail("den@mail.ru").withHomephone("+79188888777");

        //выбираем последний элемент в списке контактов для модификации
        app.contact().selectAndInitContactModification(index);

        app.contact().fillContactForm(contact, false);
        app.contact().submitContactModification();
        //app.getContactHelper().submitContactCreation();
        app.goTo().HomePage();

        //список элементов после того как будет модификация
        List<ContactData> after = app.contact().list();

        //сравнение кол-ва контактов до добавления и после
        Assert.assertEquals(after.size(), before.size());

        //модифицируем старый список - удалим из списка элемент, который ранее удаляли
        before.remove(index);
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
