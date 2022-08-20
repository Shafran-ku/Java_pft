package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

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

    public void selectGroupById(int id) {
        //находим все элементы по локатору и среди всех выбираем нужный по индексу, и делаем клик
        wd.findElement(By.cssSelector("input[value='" + id + "']")).click();
    }
    public void initGroupModification() {
        click(By.name("edit"));
    }

    public void submitGroupModification() {
        click(By.name("update"));
    }

    public void create(GroupData group) {
        initGroupCreation();
        fillGroupForm(group);
        submitGroupCreation();
        groupCache = null;
        returnToGroupPage();
    }

    //модификация группы
    public void modify(GroupData group) {
        selectGroupById(group.getId());
        initGroupModification();
        fillGroupForm(group);
        submitGroupModification();
        groupCache = null;
        returnToGroupPage();
    }

    public void delete(GroupData group) {
        selectGroupById(group.getId());
        deleteSelectedGroups();

        //сбросить кэш
        groupCache = null;

        returnToGroupPage();
    }

    public boolean isThereAGroup() {
        return isElementPresent(By.name("selected[]"));
    }

    //подсчитать кол-во элементов "selected[]" на странице (кол-во групп)
    public int getGroupCounter() {
        return wd.findElements(By.name("selected[]")).size();
    }

    //Кэш
    private Groups groupCache = null;


    public List<GroupData> list() {
        List<GroupData> groups = new ArrayList<GroupData>();
        //получаем список объектов типа web элемент (найти все элементы которые имеют тэг span и класс group)
        List<WebElement> elements = wd.findElements(By.cssSelector("span.group"));
        //проходим по каждому element
        for (WebElement element : elements) {
            //и получаем название группы
            String name = element.getText();

            //получаем идентификатор группы из элемента "span.group"
            //Integer.parseInt() - преобразования типа данных в int
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));

            //добавляем созданный объект в список
            groups.add(new GroupData().withId(id).withName(name));
        }
        return groups;
    }

    //возвращает не список а множество
    public Groups all() {
        //если кэш не пустой то вернуть его копию
        if (groupCache != null) {
            return new Groups(groupCache);
        }
        groupCache = new Groups();
        //получаем список объектов типа web элемент (найти все элементы которые имеют тэг span и класс group)
        List<WebElement> elements = wd.findElements(By.cssSelector("span.group"));
        //проходим по каждому element
        for (WebElement element : elements) {
            //и получаем название группы
            String name = element.getText();

            //получаем идентификатор группы из элемента "span.group"
            //Integer.parseInt() - преобразования типа данных в int
            int id = Integer.parseInt(element.findElement(By.tagName("input")).getAttribute("value"));

            //добавляем созданный объект в список
            groupCache.add(new GroupData().withId(id).withName(name));
        }
        return new Groups(groupCache);
    }

    //проверка наличия группы test1 при создании контакта
    public boolean isAnyGroupExist() {
        return isElementPresent(By.name("selected[]"));
    }

}