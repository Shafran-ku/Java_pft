package ru.stqa.pft.addressbook.model;

import java.util.Objects;

public class GroupData {
    //добавили идентификатор для группы
    private int id;
    private final String name;
    private final String header;
    private final String footer;

    //группа с неизвестным идентификатором
    // Конструктор который не принимает идентификатор группы id в качестве параметра,
    //если вызывается этот конструктор то присваивается id = null
    public GroupData(String name, String header, String footer) {
        //в качестве значения по-умолчанию присваиваем максимальное зн-е
        this.id = Integer.MAX_VALUE;

        this.name = name;
        this.header = header;
        this.footer = footer;
    }

    public GroupData(int id, String name, String header, String footer) {
        this.id = 0;
        this.name = name;
        this.header = header;
        this.footer = footer;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getHeader() {
        return header;
    }

    public String getFooter() {
        return footer;
    }


    @Override
    public String toString() {
        return "GroupData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    //сгенерировали метод equals для того чтобы уметь сравнивать объекты типа GroupData
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupData groupData = (GroupData) o;
        return Objects.equals(name, groupData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
