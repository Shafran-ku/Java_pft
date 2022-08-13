package ru.stqa.pft.addressbook.model;

import java.util.Objects;

public class ContactData {
    private final String firstname;
    private final String lastname;
    private final String address;
    private final String email;
    private final String homephone;
    private String group;

    public ContactData(String firstname, String lastname, String address, String email, String homephone, String group) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.email = email;
        this.homephone = homephone;
        this.group = group;
    }

    //code → generate → to string: чтобы можно было видеть содержимое элементов списка при сравнении
    @Override
    public String toString() {
        return "ContactData{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    //code → generate → equals & hashcode: сгенерировали метод equals для того чтобы уметь сравнивать объекты типа ContactData
    // (при сравнении объектов сравниваются их атрибуты)
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
}
