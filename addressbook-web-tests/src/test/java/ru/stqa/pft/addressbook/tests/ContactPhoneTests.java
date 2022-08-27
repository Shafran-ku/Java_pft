package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactPhoneTests extends TestBase {

    //предусловия теста
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();

        //проверка наличия контакта для удаления: если нечего удалять, то создать контакт
        if (app.contact().all().size() == 0)  {
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                    .withEmail("den@mail.ru").withHomePhone("+79188888777").withGroup("test1"));
        }
    }

    @Test
    public void testContactPhones() {
        app.goTo().HomePage();
        //загружаем множество контактов
        ContactData contact = app.contact().all().iterator().next();

        //выбираем контакт
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);
    }
}
