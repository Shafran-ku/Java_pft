package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Comparator;
import java.util.List;

public class GroupModificationTests extends TestBase{

    //выполнение предусловия
    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().groupPage();
        //проверка наличия хоть одной группы; если нет - создать
        if (app.group().list().size() == 0) {
            app.group().create(new GroupData("test1", null, null)); //было "test1", "test2", "test3"
        }
    }

    @Test
    public void testGroupModification() {

        //будет содержать список элементов после того как будет создана группа
        List<GroupData> before = app.group().list();

        //index - группа, которую мы собираемся модифицировать
        int index = before.size() -1;

        //при модификации группы указываем новое имя, новые header, новый footer, а идентификатор сохраняем старый
        GroupData group = new GroupData(before.get(index).getId(), "test1", "test2", "test3");
        //модификация группы
        app.group().modify(index, group);

        //будет содержать список элементов после того как будет создана группа
        List<GroupData> after = app.group().list();

        //сравниваем размеры списков
        Assert.assertEquals(after.size(), before.size());

        //модифицируем старый список - удаляем предыдущий, добавляем последний
        before.remove(index);
        before.add(group);

        //локальная переменная + лямбда  выражение(анонимая ф-я, на вход принимает 2 сравниваемых группы, и выполняет сравнение идент-ов
        Comparator<? super GroupData> byId = (g1, g2) -> Integer.compare(g1.getId(), g2.getId());

        //передаем переменную byId, сортируем первый список
        before.sort(byId);

        //сортируем второй список
        after.sort(byId);

        //сравниваем отсортированные списки
        Assert.assertEquals(before, after);
    }


}
