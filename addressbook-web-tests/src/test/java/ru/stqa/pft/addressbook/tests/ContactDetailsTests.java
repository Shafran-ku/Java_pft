package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDetailsTests extends TestBase {

    //предусловия теста
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().HomePage();

        //проверка наличия контакта для удаления: если нечего удалять, то создать контакт
        if (app.contact().all().size() == 0) {
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh.").withAddress("Suvorova st.")
                    .withEmail("den@mail.ru").withHomePhone("+79188888777"));
        }
    }

    @Test
    public void testContactPhones() {
        app.goTo().HomePage();
        //загружаем множество контактов
        ContactData contact = app.contact().all().iterator().next();

        //выбираем контакт
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);

        //сравниваем телефоны контакта, которые загружены с главной стр и стр редакт-я (все телефоны склеены в одну строку)
        assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm)));

        //+для адреса
        assertThat(contact.getAddress(), equalTo(mergeAddress(contactInfoFromEditForm)));

        //+для email
        assertThat(contact.getAllEmail(), equalTo(mergeEmail(contactInfoFromEditForm)));
    }

    private String mergePhones(ContactData contact) {
        //список из 3х элементов (телефоны)
        //из этого списка отсеем (метод filter) элементы =null,
        // очистим (метод map(ContactPhoneTests::cleaned))
        //и склеем (метод Collectors.joining)
        return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone(), contact.getPhone2())
                .stream().filter((s) -> !s.equals(""))
                .map(ContactDetailsTests::cleanedPhone)
                .collect(Collectors.joining("\n"));
    }

    //+mergeAddress (используется список из одного элемента)
    private String mergeAddress(ContactData contact) {
        return Collections.singletonList(contact.getAddress())
                .stream().filter((s) -> !s.equals(""))
                .collect(Collectors.joining("\n"));
    }

    //+mergeEmail
    private String mergeEmail(ContactData contact) {
        return Arrays.asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
                .stream().filter((s) -> !s.equals(""))
                .map(ContactDetailsTests::cleanedEmail)
                .collect(Collectors.joining("\n"));
    }

    //метод приводит формат телефона к очищенному виду (без (), -, и пробелов)
    public static String cleanedPhone(String phone) {
        //заменяем все "плохие" символы на пустую строку
        // \\s - пробел, -() -значит символы "-", "(" и ")"
        return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
    }

    //+cleanedEmail
    public static String cleanedEmail(String email) {
        //заменяем все "плохие" символы на пустую строку
        // "\\s+" - больше 1 рпобела заменяем на 1 пробел при сравнении
        return email.replaceAll("\\s+", " ");
    }
}
