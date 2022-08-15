package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.Comparator;
import java.util.List;

public class GroupCreationTests extends TestBase {

    @Test
    public void testGroupCreation() throws Exception {
        app.goTo().groupPage();

        //будет содержать список элементов до того как будет создана группа
        List<GroupData> before = app.group().list();

        GroupData group = new GroupData("test2", null, null);
        app.group().create(group);

        //будет содержать список элементов после того как будет создана группа
        List<GroupData> after = app.group().list();
        //сравниваем размеры списков
        Assert.assertEquals(after.size(), before.size() + 1);

        //компаратор - это интерфейс, объявляет методы, которые должны быть
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

