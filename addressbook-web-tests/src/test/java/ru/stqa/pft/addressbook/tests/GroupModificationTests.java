package ru.stqa.pft.addressbook.tests;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.*;
import static org.testng.Assert.assertEquals;

public class GroupModificationTests extends TestBase{

    //выполнение предусловия
    @BeforeMethod
    public void ensurePreconditions() {
        //проверка наличия хоть одной группы в БД; если нет - создать
        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            //создание группы
            app.group().create(new GroupData().withName("test1"));
        }
    }

    @Test
    public void testGroupModification() {

        //будет содержать множество элементов до того как будет создана группа
        Groups before = app.db().groups();
        GroupData modifiedGroup = before.iterator().next();

        //при модификации группы указываем новое имя, новые header, новый footer, а идентификатор сохраняем старый
        GroupData group = new GroupData()
                .withId(modifiedGroup.getId()).withName("test1").withHeader("test2").withFooter("test3");
        app.goTo().groupPage();
        //модификация группы
        app.group().modify(group);

        //сравниваем размеры
        assertThat(app.group().count(), equalTo(before.size()));

        //будет содержать множество элементов после того как будет создана группа (получаем из БД)
        Groups after = app.db().groups();

        assertThat(after, equalTo(before.without(modifiedGroup).withAdded(group)));
    }


}
