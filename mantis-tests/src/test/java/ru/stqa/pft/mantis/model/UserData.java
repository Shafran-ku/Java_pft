package ru.stqa.pft.mantis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity                                 //аннотация объявляет класс UserData привязанным к БД
@Table(name = "mantis_user_table")
//указали (name = "mantis_user_table") т.к. имя таблицы не совпадает с именем класса (UserData)
public class UserData {
    @Id
    @Column(name = "id")
    private int id = Integer.MAX_VALUE;

    @Column(name = "username")
    private String username;

    @Column(name = "realname")
    private String realname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;


    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRealname() {
        return realname;
    }

    public String getPassword() {
        return password;
    }

    public UserData withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserData withId(int id) {
        this.id = id;
        return this;
    }

    public UserData withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserData withRealName(String realName) {
        this.realname = realName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return id == userData.id && Objects.equals(username, userData.username)
                && Objects.equals(realname, userData.realname)
                && Objects.equals(email, userData.email)
                && Objects.equals(password, userData.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, realname, email, password);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", realName='" + realname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserData withEmail(String email) {
        this.email = email;
        return this;
    }
}
