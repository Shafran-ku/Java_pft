package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

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
        type(By.name("home"), contactData.getHomephone());


    //Если это создание контакта, то проверяем наличие выпадающего списка групп "new_group"
        //+проверка: если модификация контакта, то выпадающего списка групп быть не должно
        //если это форма создания
        if (creation) {
            if (isElementPresent(By.name(contactData.getGroup()))) {
                //выбрать из списка групп какой нибудь элемент
                new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
            } else new Select(wd.findElement(By.name("new_group"))).getFirstSelectedOption();
        //иначе проверить отсутствие выпадающего списка "new_group"
        } else Assert.assertFalse(isElementPresent(By.name("new_group")));
    }

    public void selectContactById(int id)  {
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

            //добавляем созданный объект в contact
            contactCache.add(new ContactData().withId(id).withFirstname(cells.get(2).getText())
                    .withLastname(cells.get(1).getText()));
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
        initContactModificationById(contact.getId());
        //извлекаем атрибуты
        String firstname = wd.findElement(By.name("firstname")).getAttribute("value");
        String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
        String home = wd.findElement(By.name("home")).getAttribute("value");
        String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
        String work = wd.findElement(By.name("work")).getAttribute("value");
        wd.navigate().back();
        //создаем объект ContactData с полученными атрибутами
        return new ContactData().withId(contact.getId()).withFirstname(firstname).withLastname(lastname)
                .withHomePhone(home).withMobilePhone(mobile).withWorkPhone(work);
    }

    //в качестве параметра принимает идентификатор контакта
    public void initContactModificationById(int id) {
        //wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
        //click(By.xpath("//img[@alt='Edit']"));
        //wd.findElement(By.xpath("//a[@href='edit.php?id=" + id + "'" + "]")).click();     выключил в 5.9

        //метод String.format - делает подстановку значений внутрь строки (вместо %s подставится значение)
        /*
        WebElement checkbox = wd.findElement(By.cssSelector(String.format("input[value='%s']", id)));
        WebElement row = checkbox.findElement(By.xpath("./../.."));
        List<WebElement> cells = row.findElements(By.tagName("td"));
        cells.get(7).findElement(By.tagName("a")).click();
        */

        //поиск по идентификатору в ссылке на редактирование, и нажатие на карандашик (%s - здесь подставится значение)
        wd.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']", id))).click();
    }
}

