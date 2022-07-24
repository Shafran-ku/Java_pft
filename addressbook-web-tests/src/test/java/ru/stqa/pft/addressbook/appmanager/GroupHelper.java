package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.ArrayList;
import java.util.List;

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

    public List<GroupData> getGroupList() {
        List<GroupData> groups = new ArrayList<GroupData>();
        //получаем список объектов типа web элемент (найти все элементы которые имеют тэг span и класс group)
        List<WebElement> elements = wd.findElements(By.cssSelector("span.group"));
        //проходим по каждому element
        for (WebElement element : elements) {
            //и получаем название группы
            String name = element.getText();
            GroupData group = new GroupData(name, null, null);
            //добавляем созданный объект в список
            groups.add(group);
        }
        return groups;
    }

    //проверка наличия группы test1 при создании контакта
    public boolean isAnyGroupExist() {
        return isElementPresent(By.name("selected[]"));
    }
}
