package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.HashSet;
import java.util.List;

public class GroupModificationTests extends TestBase{
    @Test
    public void testGroupModification() {
        app.getNavigationHelper().gotoGroupPage();
        //проверка наличия хоть одной группы; если нет - создать
        if (! app.getGroupHelper().isThereAGroup()) {
            app.getGroupHelper().createGroup(new GroupData("test1", null, null)); //было "test1", "test2", "test3"
        }
        //будет содержать список элементов после до того как будет создана группа
        List<GroupData> before = app.getGroupHelper().getGroupList();
        app.getGroupHelper().selectGroup(before.size() -1);
        app.getGroupHelper().initGroupModification();

        //сделали переменную, чтобы не писать одно и то же
        //при модификации группы указываем имя, новые header, новый footer, а идентификатор сохраняем старый
        GroupData group = new GroupData(before.get(before.size() -1).getId(), "test1", "test2", "test3");  //было "test1", null, null

        app.getGroupHelper().fillGroupForm(group);
        app.getGroupHelper().submitGroupModification();
        app.getGroupHelper().returnToGroupPage();
        //будет содержать список элементов после того как будет создана группа
        List<GroupData> after = app.getGroupHelper().getGroupList();
        //сравниваем размеры списков
        Assert.assertEquals(after.size(), before.size());

        //модифицируем старый список - удаляем предыдущий, добавляем последний
        before.remove(before.size() - 1);
        before.add(group);
        //сравниваем списки, но перед сравнением преобразуем списки в множества и сравниваем их
        Assert.assertEquals(new HashSet<Object>(before), new HashSet<Object>(after));
    }
}
