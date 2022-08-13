package ru.stqa.pft.addressbook.model;

import java.util.Objects;

public class ContactData {
    private int id;
    private final String firstname;
    private final String lastname;
    private final String address;
    private final String email;
    private final String homephone;
    private String group;




    // Конструктор который не принимает идентификатор id контакта в качестве параметра,
    // если вызывается этот констр-р то присваивается null в ккачестве id (контакт с неизвестным id, т.е. созданный вручную, а не считанный)
    public ContactData(String firstname, String lastname, String address, String email, String homephone, String group) {
        this.id = Integer.MAX_VALUE;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.email = email;
        this.homephone = homephone;
        this.group = group;
    }

    // Конструктор который принимает идентификатор id контакта в качестве параметра
    public ContactData(int id, String firstname, String lastname, String address, String email, String homephone, String group) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.email = email;
        this.homephone = homephone;
        this.group = group;
    }


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getHomephone() {
        return homephone;
    }

    public String getGroup() {
        return group;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ContactData{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactData that = (ContactData) o;
        return Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname);
    }
}
