package ru.stqa.pft.addressbook.tests;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class ContactDeletionTests extends TestBase {
    //предусловия теста

    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();

        //проверка наличия контакта для удаления: если нечего удалять, то создать контакт

        if (app.db().contacts().size() == 0)  {
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                    .withEmail("den@mail.ru").withHomePhone("+79188888777"));
        }
    }


    @Test
    public void testContactDeletion() {

        // до удаления
        Contacts before = app.db().contacts();
        ContactData deletedContact = before.iterator().next();
        app.contact().delete(deletedContact);
        app.goTo().HomePage();

        //сравнение размера до и после добавления
        assertThat(app.contact().count(), equalTo(before.size() - 1));

        //после удаления контакта
        Contacts after = app.db().contacts();

        //сравниваем по содержимому
        assertThat(after, equalTo(before.without(deletedContact)));

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyContactListInUI();
       }

}

