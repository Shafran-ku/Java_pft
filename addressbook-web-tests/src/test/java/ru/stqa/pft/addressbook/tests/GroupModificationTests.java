package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GroupModificationTests extends TestBase{

    //выполнение предусловия
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().groupPage();
        //проверка наличия хоть одной группы; если нет - создать
        if (app.group().all().size() == 0) {
            app.group().create(new GroupData().withName("test1"));
        }
    }

    @Test
    public void testGroupModification() {

        //будет содержать множество элементов после того как будет создана группа
        Set<GroupData> before = app.group().all();
        GroupData modifiedGroup = before.iterator().next();

        //при модификации группы указываем новое имя, новые header, новый footer, а идентификатор сохраняем старый
        GroupData group = new GroupData()
                .withId(modifiedGroup.getId()).withName("test1").withHeader("test2").withFooter("test3");
        //модификация группы
        app.group().modify(group);

        //будет содержать список элементов после того как будет создана группа
        Set<GroupData> after = app.group().all();

        //сравниваем размеры списков
        Assert.assertEquals(after.size(), before.size());

        //модифицируем старый список - удаляем предыдущий, добавляем последний
        before.remove(modifiedGroup);
        before.add(group);

        //сравниваем отсортированные списки
        Assert.assertEquals(before, after);
    }


}
