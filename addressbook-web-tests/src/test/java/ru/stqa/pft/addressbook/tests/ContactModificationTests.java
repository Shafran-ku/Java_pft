package ru.stqa.pft.addressbook.tests;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class ContactModificationTests extends TestBase {
   //предусловия теста
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();

        //проверка есть ли контакт, если нет то проверка наличия группы
        if (app.db().contacts().size() == 0) {
            app.goTo().groupPage();
            //проверка есть ли хоть одна группа, если нет - создаем
            if (app.db().groups().size() == 0) {
                app.group().create(new GroupData().withName("test1"));
            }
            //если нет контакта, но есть группа, создаем контакт
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                    .withEmail("den@mail.ru").withHomePhone("+79188888777"));
        }
    }


    @Test
    public void testContactModification() {

        // до модификации
        Contacts before = app.db().contacts();

        ContactData modifiedContact = before.iterator().next();

        //при модификации контакта указываем новые данные, а идентификатор сохраняем старый
        ContactData contact =  new ContactData().withId(modifiedContact.getId()).withFirstname("Den")
                .withLastname("Kh.").withAddress("Suvorova st.").withEmail("den@mail.ru").withHomePhone("+79188888777");

        app.contact().modify(contact);
        app.goTo().HomePage();

        //сравнение размера до и после добавления
        assertThat(app.contact().count(), equalTo(before.size()));

        //после того как будет модификация
        Contacts after = app.db().contacts();

        //сравниваем по содержимому
        assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyContactListInUI();
    }


}
