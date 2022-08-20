package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Set;

public class ContactDeletionTests extends TestBase {
    //предусловия теста
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();

        //проверка наличия контакта для удаления: если нечего удалять, то создать контакт
        if (app.contact().all().size() == 0)  {
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                    .withEmail("den@mail.ru").withHomephone("+79188888777").withGroup("test1"));
        }
    }

    @Test
    public void testContactDeletion() {

        //множество элементов до удаления
        Set<ContactData> before = app.contact().all();
        ContactData deletedContact = before.iterator().next();
        app.contact().delete(deletedContact);
        app.goTo().HomePage();

        //список элементов после удаления контакта
        Set<ContactData> after = app.contact().all();

        //сравнение кол-ва контактов до добавления и после
        Assert.assertEquals(after.size(), before.size() - 1);
        
        //удаляем ненужный элемент из списка для сравнения
        before.remove(deletedContact);

        //сравниваем 2 списка
        Assert.assertEquals(before, after);
        }

}

