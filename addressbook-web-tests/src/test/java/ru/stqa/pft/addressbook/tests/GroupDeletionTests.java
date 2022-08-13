package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;

public class GroupDeletionTests extends TestBase {

    @Test
    public void testGroupDeletion() throws Exception {
        app.getNavigationHelper().gotoGroupPage();
        //проверка наличия хоть одной группы; если нет - создать
        if (! app.getGroupHelper().isThereAGroup()) {
            app.getGroupHelper().createGroup(new GroupData("test1", "test2", "test3"));
        }
        //будет содержать список элементов после до того как будет создана группа
        List<GroupData> before = app.getGroupHelper().getGroupList();
        app.getGroupHelper().selectGroup(before.size() -1);
        app.getGroupHelper().deleteSelectedGroups();
        app.getGroupHelper().returnToGroupPage();
        //будет содержать список элементов после того как будет создана группа
        List<GroupData> after = app.getGroupHelper().getGroupList();
        //сравниваем размеры списков (размер нового списка на 1 меньше старого, поэтому от старого отнимаем 1)
        Assert.assertEquals(after.size(), before.size() -1);

        //аналогично удаляем элемент из списка before, чтобы кол-во элементов списка before = after
        before.remove(before.size() - 1);
        //проверка совпадений элементов списков, в качестве параметров передаются 2 списка
        Assert.assertEquals(before, after);

    }
}

