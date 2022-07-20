package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.stqa.pft.addressbook.model.GroupData;

public class GroupHelper extends HelperBase {

    public GroupHelper(WebDriver wd) {
        super(wd);
    }

    public void returnToGroupPage() {
        click(By.linkText("group page"));
    }

    public void submitGroupCreation() {
        click(By.name("submit"));
    }

    public void fillGroupForm(GroupData groupData) {
        type(By.name("group_name"), groupData.getName());
        type(By.name("group_header"), groupData.getHeader());
        type(By.name("group_footer"), groupData.getFooter());
    }

    public void initGroupCreation() {
        click(By.name("new"));
    }

    public void deleteSelectedGroups() {
        click(By.name("delete"));
    }

    //передаем в качестве параметра индекс элемента
    public void selectGroup(int index) {
        //находим все элементы по локатору и среди всех выбираем нужный по индексу, и делаем клик
        wd.findElements(By.name("selected[]")).get(index).click();
    }

    public void selectGroup(int index) {
        //находим все элементы по локатору и среди всех выбираем нужный по индексу, и делаем клик
        wd.findElements(By.name("selected[]")).get(index).click();
    }


    public void initGroupModification() {
        click(By.name("edit"));
    }

    public void submitGroupModification() {
        click(By.name("update"));
    }

    public void createGroup(GroupData group) {
        initGroupCreation();
        fillGroupForm(group);
        submitGroupCreation();
        returnToGroupPage();
    }

    public boolean isThereAGroup() {
        return isElementPresent(By.name("selected[]"));
    }

    //подсчитать кол-во элементов "selected[]" на странице (кол-во групп)
    public int getGroupCounter() {
        return wd.findElements(By.name("selected[]")).size();
    }
}
