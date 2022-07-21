package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;

public class GroupCreationTests extends TestBase {

    @Test
    public void testGroupCreation() throws Exception {
        app.getNavigationHelper().gotoGroupPage();
        //будет содержать список элементов после до того как будет создана группа
        List<GroupData> before = app.getGroupHelper().getGroupList();
        app.getGroupHelper().createGroup(new GroupData("test1", null, null));
        //будет содержать список элементов после того как будет создана группа
        List<GroupData> after = app.getGroupHelper().getGroupList();
        //сравниваем размеры списков
        Assert.assertEquals(after.size(), before.size() + 1);

    }

}

