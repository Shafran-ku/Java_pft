
package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactFromGroup extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        //если нет ни одной группы то создать
        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("test 1"));
        }

        //если нет ни одного контакта то создать
        if (app.db().contacts().size() == 0) {
            app.goTo().HomePage();
            app.contact().create(new ContactData().withFirstname("Den").withLastname("Kh."));
        }
    }

    @Test
    public void testDeleteContactFromGroup() {
        //до удаления контакта из группы
        Contacts before = app.db().contacts();

        app.goTo().HomePage();

        //выбор контакта
        ContactData selectContact = before.iterator().next();

        //новая группа "test 1"
        GroupData group = new GroupData().withName("test 1");
        app.contact().selectGroup(group);


        ContactData contact = new ContactData().withId(selectContact.getId());

        //если нет ни одного контакта в БД или нет контактов в группе "test 1"
        if (app.db().contacts().size() == 0
                //находим все контакты по локатору и среди всех ищем нужный по индексу
                || !app.contact().isElementPresent(By.cssSelector("input[value = '" + contact.getId() + "']"))) {
            //включаем все контакты [all]
            app.contact().checkAllPage();

            //выбираем контакт
            app.contact().selectContactById(contact.getId());
            //добавляем в доступную группу
            app.contact().addToGroup(group);

            //возврашаемся и выбираем группу
            app.goTo().HomePage();
            app.contact().selectGroup(group);
        }

        //удаляем контакт из группы
        app.contact().deleteContactFromGroup(contact);
        app.goTo().HomePage();

        //проверяем
        app.contact().checkContactInGroup(group);

        //после удаления контакта из группы
        Contacts after = app.db().contacts();

        //проверяем что кол-во контактов до и после совпадает
        assertThat(after, equalTo(before));

        //проверка загрузки данных из UI для тестов,
        //возможность отключать проверку с ui-через конфигуратор: в VM options добавить: -DverifyUI=true
        verifyContactListInUI();

    }
}
