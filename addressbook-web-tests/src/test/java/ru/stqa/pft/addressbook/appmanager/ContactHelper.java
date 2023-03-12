package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;

public class ContactHelper extends HelperBase {

    //обращение к конструктору базового класса HelperBase
    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void returnToHomePage() {
        click(By.linkText("home page"));
    }

    public void submitContactCreation() {
        click(By.xpath("//div[@id='content']/form/input[21]"));
    }

    //вторым параметром передаем инф-ию о том, является ли это заполнение формы нового контакта или нет(true:да, false:нет)
    public void fillContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstname());
        click(By.name("firstname"));
        wd.findElement(By.name("lastname")).clear();
        wd.findElement(By.name("lastname")).sendKeys(contactData.getLastname());
        type(By.name("address"), contactData.getAddress());
        type(By.name("email"), contactData.getEmail());
        type(By.name("home"), contactData.getHomePhone());

        //добавляем также фото с полным путем к файлу
        attach(By.name("photo"), contactData.getPhoto());

        //Если это создание контакта, то проверяем наличие выпадающего списка групп "new_group"
        //+проверка: если модификация контакта, то выпадающего списка групп быть не должно
        //если это форма создания
        if (creation) {
            if (contactData.getGroups().size() > 0) {
                //проверка если указана 1 группа, то добавляем, если !=1 то не добавляем
                Assert.assertTrue(contactData.getGroups().size() == 1);
                //выбрать из списка групп какой нибудь элемент
                //iterator().next().getName()- извлекаем какую то группу и берем у нее имя
                new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroups().iterator().next().getName());
            } else new Select(wd.findElement(By.name("new_group"))).getFirstSelectedOption();
            //иначе проверить отсутствие выпадающего списка "new_group"
        } else Assert.assertFalse(isElementPresent(By.name("new_group")));
    }

    public void selectContactById(int id) {
        wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
    }

    public void initContactCreation() {
        click(By.linkText("add new"));
    }

    //выбор контакта и нажатие на иконку "редактировать"
    public void modify(ContactData contact) {
        //wd.findElements(By.xpath("//img[@alt='Edit']")).get(contact).click();
        //click(By.xpath("//img[@alt='Edit']"));
        selectContactById(contact.getId());
        initContactModificationById(contact.getId());
        fillContactForm(contact, false);
        submitContactModification();
        contactCache = null;
    }

    public void submitContactModification() {
        click(By.name("update"));
    }

    public void deleteSelectedContact() {
        click(By.xpath("//input[@value='Delete']"));
    }

    public void create(ContactData contact) {
        initContactCreation();
        fillContactForm(contact, true);
        submitContactCreation();
        contactCache = null;
        returnToHomePage();
    }

    public void delete(ContactData contact) {
        selectContactById(contact.getId());
        deleteSelectedContact();
        contactCache = null;
        isAlertAccept();
    }

    //Кэш
    private Contacts contactCache = null;

    //возвращается множество
    public Contacts all() {
        //если кэш не пустой то вернуть его копию
        if (contactCache != null) {
            return new Contacts(contactCache);
        }
        contactCache = new Contacts();

        //получаем список объектов
        List<WebElement> elements = wd.findElements(By.name("entry"));

        //пройти в цикле по элементам (строкам таблицы)
        for (WebElement element : elements) {
            //и из каждого получить text:  имя + фамилия контакта
            //String name = element.getText();
            List<WebElement> cells = element.findElements(By.tagName("td"));

            //получаем идентификатор для каждого контакта(поиск элемента "id" по тегу "input" внутри элемента "entry");
            //Integer.parseInt() - преобразования типа данных в int
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("id"));

            String lastname = cells.get(1).getText();
            String firstname = cells.get(2).getText();
            String address = cells.get(3).getText();
            String allEmail = cells.get(4).getText();
            String allPhones = cells.get(5).getText();

            //добавляем созданный объект в contact
            contactCache.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname)
                    .withAddress(address)
                    .withAllEmail(allEmail)
                    .withAllPhones(allPhones));
        }
        return new Contacts(contactCache);
    }

    public boolean isThereAnyContact() {
        return isElementPresent(By.name("selected[]"));
    }

    public int count() {
        return wd.findElements(By.name("entry")).size();
    }

    public ContactData infoFromEditForm(ContactData contact) {
        //выбор контакта по идентификатору
        initContactModificationById(contact.getId());
        //извлекаем атрибуты
        String firstname = wd.findElement(By.name("firstname")).getAttribute("value");
        String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
        String address = wd.findElement(By.name("address")).getAttribute("value");
        String home = wd.findElement(By.name("home")).getAttribute("value");
        String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
        String work = wd.findElement(By.name("work")).getAttribute("value");
        String phone2 = wd.findElement(By.name("phone2")).getAttribute("value");
        String email = wd.findElement(By.name("email")).getAttribute("value");
        String email2 = wd.findElement(By.name("email2")).getAttribute("value");
        String email3 = wd.findElement(By.name("email3")).getAttribute("value");
        wd.navigate().back();
        //создаем объект ContactData с полученными атрибутами
        return new ContactData().withId(contact.getId()).withFirstname(firstname).withLastname(lastname)
                .withAddress(address)
                .withHomePhone(home)
                .withMobilePhone(mobile)
                .withWorkPhone(work)
                .withPhone2(phone2)
                .withEmail(email)
                .withEmail2(email2)
                .withEmail3(email3);
    }

    //в качестве параметра принимает идентификатор контакта
    public void initContactModificationById(int id) {
        //wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
        //click(By.xpath("//img[@alt='Edit']"));
        //wd.findElement(By.xpath("//a[@href='edit.php?id=" + id + "'" + "]")).click();     выключил в 5.9

        /* один из методов построения локатора***********************************************************************
        //метод String.format - делает подстановку значений внутрь строки (вместо %s подставится значение)//
        //ищем чекбокс в строке контакта (элемент с тегом input и заданным атрибутом)
        WebElement checkbox = wd.findElement(By.cssSelector(String.format("input[value='%s']", id)));

        //1.выполняем поиск нужной строки с чекбоксом - переход к родительскому элементу чекбокса - 2 прыжка вверх (на ячейку, потом к строке таблицы)
        WebElement row = checkbox.findElement(By.xpath("./../.."));

        //2.нужно попасть в ячейку с иконкой карандаша:
        //берем все элементы и ищем внутри элемент с тегом td
        List<WebElement> cells = row.findElements(By.tagName("td"));

        //3.среди найденных ячеек берем по номеру нужную (карандашик в 7ом столбце),
        //находим в ней ссылку с тегом "a" и кликаем по ней
        cells.get(7).findElement(By.tagName("a")).click();
        */

        //+ один из методов построения локатора-находим нужный элемент сразу******************************************
        //поиск по идентификатору в ссылке на редактирование, и нажатие на карандашик (%s - здесь подставится значение)
        //+ здесь использован метод String.format - делает подстановку значений внутрь строки (вместо %s подставится значение
        wd.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']", id))).click();
    }

    //смотрим контакты без групп
    public void showContactsWithoutGroup() {

        wd.findElement(By.name("group")).click();
        new Select(wd.findElement(By.name("group"))).selectByVisibleText("[none]");
    }

    //выбрать группу
    public void selectGroupForContact(ContactData contact, GroupData group) {
        if (contact.getGroups().size() > 0)
            Assert.assertTrue(contact.getGroups().size() == 1);
        new Select(wd.findElement(By.name("to_group"))).selectByVisibleText(group.getName());
    }

    //добавление контакта в группу
    public void addContactToGroup(ContactData contact, GroupData group) {
        selectContactById(contact.getId());
        selectGroupForContact(contact, group);
        addInGroup();
    }

    //добавить в группу
    public void addInGroup() {
        click(By.name("add"));
    }

    //удалить из группы
    public void removeContactFromGroup(ContactData contact) {
        new Select(wd.findElement(By.name("group"))).selectByVisibleText(contact.getGroups().iterator().next().getName());
        selectContactById(contact.getId());
        removeFromGroup();
    }

    private void removeFromGroup() {
        click(By.name("remove"));
    }
}


