package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity                                 //аннотация объявляет класс ContactData привязанным к БД
@Table(name = "addressbook")
//указали (name = "addressbook") т.к. имя таблицы не совпадает с именем класса (ContactData)
public class ContactData {
    @Id
    @Column(name = "id")
    private int id = Integer.MAX_VALUE;

    @Column(name = "firstname")
    @Expose //пометка полей, которые оставляем в json
    private String firstname;

    @Column(name = "lastname")
    @Expose
    private String lastname;

    @Expose
    @Type(type = "text")            //указали тип полей, которые не могут преобразоваться автоматически
    private String address;

    @Transient
    private String allEmail;

    @Transient
    @Expose
    private String email;

    @Transient
    private String email2;

    @Transient
    private String email3;

    @Column(name = "home")
    @Type(type = "text")            //указали тип полей, которые не могут преобразоваться автоматически
    @Expose
    private String homePhone;

    @Column(name = "mobile")
    @Type(type = "text")
    private String mobilePhone;

    @Column(name = "work")
    @Type(type = "text")            //указали тип полей, которые не могут преобразоваться автоматически
    private String workPhone;

    @Type(type = "text")
    private String phone2;

    @Transient
    private String allPhones;

    //@JoinTable:
    //  name = "address_in_groups" - в качестве связующей указали таблицу
    //  joinColumns = @JoinColumn(name = "id") - указывает на объект текущего класса- на контакты
    //  inverseJoinColumns = @JoinColumn(name = "group_id") - указывает на объект другого типа - на группу
    //FetchType.EAGER - из БД извлекается много инф-ии за один заход
    @ManyToMany(fetch = FetchType.EAGER)
    //описываем связь между объектами 2 типов
    @JoinTable(name = "address_in_groups",
            joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<GroupData> groups = new HashSet<GroupData>();

    @Override
    public String toString() {
        return "ContactData{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    @Column(name = "photo")
    @Type(type = "text")
    private String photo;

    @Override                               //делается через Generate → Equals and hashCode
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactData that = (ContactData) o;
        return id == that.id && Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname);
    }

    @Override                               //делается через Generate → Equals and hashCode
    public int hashCode() {
        return Objects.hash(id, firstname, lastname);
    }

    public File getPhoto() {
        if (photo != null) {
            return new File(photo);
        } else {
            return null;
        }
    }

    public ContactData withPhoto(File photo) {
        this.photo = photo.getPath();
        return this;
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

    public String getEmail2() {
        return email2;
    }

    public String getEmail3() {
        return email3;
    }

    public String getAllEmail() {
        return allEmail;
    }

    public String getAllPhones() {
        return allPhones;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public String getPhone2() {
        return phone2;
    }

    //новый Groups для ManyToMany
    public Groups getGroups() {
        return new Groups(groups);
    }

    public void setGroups(Set<GroupData> groups) {
        this.groups = groups;
    }

    public ContactData withId(int id) {
        this.id = id;
        return this;
    }

    public ContactData withFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public ContactData withLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public ContactData withAddress(String address) {
        this.address = address;
        return this;
    }

    public ContactData withEmail(String email) {
        this.email = email;
        return this;
    }

    public ContactData withEmail2(String email2) {
        this.email2 = email2;
        return this;
    }

    public ContactData withEmail3(String email3) {
        this.email3 = email3;
        return this;
    }

    public ContactData withAllEmail(String allEmail) {
        this.allEmail = allEmail;
        return this;
    }

    public ContactData withHomePhone(String homePhone) {
        this.homePhone = homePhone;
        return this;
    }

    public ContactData withMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public ContactData withWorkPhone(String workPhone) {
        this.workPhone = workPhone;
        return this;
    }

    public ContactData withPhone2(String phone2) {
        this.phone2 = phone2;
        return this;
    }

    public ContactData withAllPhones(String allPhones) {
        this.allPhones = allPhones;
        return this;
    }

    public int getId() {
        return id;
    }

    public ContactData inGroup(GroupData group) {
        groups.add(group);
        return this;
    }
}
