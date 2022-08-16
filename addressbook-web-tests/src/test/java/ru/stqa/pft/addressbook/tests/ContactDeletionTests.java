package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.List;

public class ContactDeletionTests extends TestBase {
    //предусловия теста
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();

        //проверка наличия контакта для удаления: если нечего удалять, то создать контакт
        if (app.contact().list().size() == 0)  {
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                    .withEmail("den@mail.ru").withHomephone("+79188888777").withGroup("test1"));
        }
    }

    @Test
    public void testContactDeletion() {

        //список элементов до удаления
        List<ContactData> before = app.contact().list();

        int index = before.size() - 1;
        app.contact().delete(index);
        app.goTo().HomePage();

        //список элементов после удаления контакта
        List<ContactData> after = app.contact().list();

        //сравнение кол-ва контактов до добавления и после
        Assert.assertEquals(after.size(), before.size() - 1);

        
        //удаляем ненужный элемент из списка для сравнения
        before.remove(index);

        //сравниваем 2 списка
        Assert.assertEquals(before, after);
        }

}

