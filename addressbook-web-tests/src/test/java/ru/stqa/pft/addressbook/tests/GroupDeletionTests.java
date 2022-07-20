package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

public class GroupDeletionTests extends TestBase {

    @Test
    public void testGroupDeletion() throws Exception {
        app.getNavigationHelper().gotoGroupPage();
        //получить список групп до теста
        int before = app.getGroupHelper().getGroupCounter();
        //проверка наличия хоть одной группы; если нет - создать
        if (! app.getGroupHelper().isThereAGroup()) {
            app.getGroupHelper().createGroup(new GroupData("test1", "test2", "test3"));
        }
        app.getGroupHelper().selectGroup(before -1);
        app.getGroupHelper().deleteSelectedGroups();
        app.getGroupHelper().returnToGroupPage();
        //получить список груп после теста
        int after = app.getGroupHelper().getGroupCounter();
        //проверка, что кол-во групп уменьшилось на 1
        Assert.assertEquals(after, before -1);
    }
}

