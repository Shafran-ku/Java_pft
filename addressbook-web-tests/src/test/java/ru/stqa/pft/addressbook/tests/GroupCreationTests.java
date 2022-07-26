package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.HashSet;
import java.util.List;

public class GroupCreationTests extends TestBase {

    @Test
    public void testGroupCreation() throws Exception {
        app.getNavigationHelper().gotoGroupPage();

        //будет содержать список элементов после до того как будет создана группа
        List<GroupData> before = app.getGroupHelper().getGroupList();

        GroupData group = new GroupData("test1", null, null);
        app.getGroupHelper().createGroup(group);

        //будет содержать список элементов после того как будет создана группа
        List<GroupData> after = app.getGroupHelper().getGroupList();
        //сравниваем размеры списков
        Assert.assertEquals(after.size(), before.size() + 1);


        //среди всех элементов списка найти максимальный идентификатор(будет у последней добавленной группы)
        int max = 0;
        for (GroupData g: after) {
            if (g.getId() > max) {
                max = g.getId();
            }
        }
        group.setId(max);
        before.add(group);
        Assert.assertEquals(new HashSet<Object>(before), new HashSet<Object>(after));

    }

}

