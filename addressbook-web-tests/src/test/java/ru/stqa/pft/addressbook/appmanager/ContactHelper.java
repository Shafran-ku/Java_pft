package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;

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

    public void initContactCreation() {
        click(By.linkText("add new"));
    }

    public void selectAndInitContactModification() {
        click(By.xpath("//img[@alt='Edit']"));
    }


    public void deleteSelectedContact() {
        click(By.xpath("//div[@id='content']/form[2]/input[2]"));

    }

    public void createContact(ContactData contact) {
        initContactCreation();
        fillContactForm(contact, true);
        submitContactCreation();
        returnToHomePage();
    }

    /*

    //проверка наличия иконки редактирования (чтобы понять есть ли хоть один контакт для удаления на форме)
    public boolean isThereAGroup() {
        return isElementPresent(By.xpath("//img[@alt='Edit']"));
    }

     */

    public boolean isThereAnyContact() {
        return isElementPresent(By.name("selected[]"));
    }
}

