package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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

    public void fillContactForm(ContactData contactData) {
        type(By.name("firstname"), contactData.getFirstname());
        click(By.name("firstname"));
        wd.findElement(By.name("lastname")).clear();
        wd.findElement(By.name("lastname")).sendKeys(contactData.getLastname());
        type(By.name("address"), contactData.getAddress());
        type(By.name("email"), contactData.getEmail());
        type(By.name("home"), contactData.getHomephone());
    }

    public void initContactCreation() {
        click(By.linkText("add new"));
    }

    public void selectAndInitContactModification() {
        click(By.xpath("//img[@alt='Edit']"));
    }

    public void goToHomePage() {
        click(By.linkText("home"));
    }

    public void     updateContactForm() {
        click(By.linkText("update"));
        //click(By.xpath("//div[@id='content']/form/input[22]"));

    }
}
