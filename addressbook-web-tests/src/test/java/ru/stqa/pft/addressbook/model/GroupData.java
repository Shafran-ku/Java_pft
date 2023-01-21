package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.Type;
import org.testng.annotations.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@XStreamAlias("group")


@Entity                         //аннотация объявляет класс GroupData привязанным к БД

@Table(name = "group_list")     //указали (name = "group_list") т.к. имя таблицы не совпадает с именем класса (GroupData)
public class GroupData {
    @XStreamOmitField           //указываем что не нужно сохранять в xml идентификатор id (@XStreamOmitField означает пропустить поле)
    @Id
    @Column(name = "group_id")

    //добавили идентификатор для группы
    private int id = Integer.MAX_VALUE;

    @Expose //пометка полей, которые оставляем в json
    @Column(name = "group_name")
    private String name;

    @Expose //пометка полей, которые оставляем в json
    @Column(name = "group_header")
    @Type(type = "text")        //нужна подсказка для правильного приведения типа
    private String header;

    @Expose //пометка полей, которые оставляем в json
    @Column(name = "group_footer")
    @Type(type = "text")        //нужна подсказка для правильного приведения типа
    private String footer;

    public int getId() {
        return id;
    }

    public GroupData withId(int id) {
        this.id = id;
        return this;
    }

    public GroupData withName(String name) {
        this.name = name;
        return this;
    }

    public GroupData withHeader(String header) {
        this.header = header;
        return this;
    }

    public GroupData withFooter(String footer) {
        this.footer = footer;
        return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupData groupData = (GroupData) o;
        return id == groupData.id && Objects.equals(name, groupData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
//аннотация для именования объектов в файле xml "group"
