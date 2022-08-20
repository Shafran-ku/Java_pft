package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Set;

public class ContactModificationTests extends TestBase {
   //предусловия теста
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();

        //проверка есть ли контакт, если нет то проверка наличия группы
        if (app.contact().all().size() == 0) {
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

        //множество элементов до модификации
        Set<ContactData> before = app.contact().all();

        ContactData modifiedContact = before.iterator().next();

        //при модификации контакта указываем новые данные, а идентификатор сохраняем старый
        ContactData contact =  new ContactData().withId(modifiedContact.getId()).withFirstname("Den")
                .withLastname("Kh.").withAddress("Suvorova st.").withEmail("den@mail.ru").withHomephone("+79188888777");

        app.contact().modify(contact);
        app.goTo().HomePage();

        //множество элементов после того как будет модификация
        Set<ContactData> after = app.contact().all();

        //сравнение кол-ва контактов до добавления и после
        Assert.assertEquals(after.size(), before.size());

        //модифицируем старый список - удалим из списка элемент, который ранее удаляли
        before.remove(modifiedContact);
        //а вместо удаленного добавим элемент, который должен появиться после модификации
        before.add(contact);

        Assert.assertEquals(before, after);
    }

}
