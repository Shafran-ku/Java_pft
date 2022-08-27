package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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

        //сравниваем телефоны на главной странице и странице редактирования (с очисткой от символов- метод cleaned)
        assertThat(contact.getHomePhone(), equalTo(cleaned(contactInfoFromEditForm.getHomePhone())));
        assertThat(contact.getMobilePhone(), equalTo(cleaned(contactInfoFromEditForm.getMobilePhone())));
        assertThat(contact.getWorkPhone(), equalTo(cleaned(contactInfoFromEditForm.getWorkPhone())));
    }

    //делаем метод, он будет приводить формат телефона к очищенному виду (без (), -, и пробелов)
    public String cleaned(String phone) {
        //заменяем все "плохие" символы на пустую строку
        // \\s - пробел, -() -значит символы "-", "(" и ")"
        return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
    }
}
