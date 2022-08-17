package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;
import java.util.Set;

public class GroupDeletionTests extends TestBase {

    //выполнение предусловия
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().groupPage();
        //проверка наличия хоть одной группы; если нет - создать
        if (app.group().all().size() ==0) {
            app.group().create(new GroupData().withName("test1"));
        }
    }

    @Test
    public void testGroupDeletion() throws Exception {

        //будет содержать множество элементов после до того как будет создана группа
        Set<GroupData> before = app.group().all();
        GroupData deletedGroup = before.iterator().next();
        app.group().delete(deletedGroup);
        //будет содержать множество элементов после того как будет создана группа
        Set<GroupData> after = app.group().all();
        //сравниваем размеры списков (размер нового списка на 1 меньше старого, поэтому от старого отнимаем 1)
        Assert.assertEquals(after.size(), before.size() -1);

        //аналогично удаляем элемент из списка before, чтобы кол-во элементов списка before = after
        before.remove(deletedGroup);
        //проверка совпадений элементов списков, в качестве параметров передаются 2 списка
        Assert.assertEquals(before, after);

    }


}

