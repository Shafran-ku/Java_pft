package ru.stqa.pft.addressbook.model;

import com.google.common.collect.ForwardingSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Contacts extends ForwardingSet<ContactData> {

    private Set<ContactData> delegate;

    //берем множество из существующего объекта, который передан в качестве параметра,
    //строим новое множество, которое содержит те же самые элементы
    //и присваимаем это новое множество в качестве атрибута в новый создаваемый этим конструктором объект
    public Contacts(Contacts contacts) {
        this.delegate = new HashSet<ContactData>(contacts.delegate());
    }

    //конструктор без параметров
    public Contacts() {
        this.delegate = new HashSet<ContactData>();
    }

    //конструктор по произвольной коллекции строит объект типа Contacts
    public Contacts(Collection<ContactData> contacts) {
        //строим новый HashSet (множество объектов типа ContactData) из коллекции (копируем)
        this.delegate = new HashSet<ContactData>(contacts);

    }

    @Override
    protected Set<ContactData> delegate() {
        return delegate;
    }

    //метод withAdded возвращает новый объект с добавленным новым контактом (копирование метода)
    public Contacts withAdded(ContactData contact) {
        Contacts contacts = new Contacts(this);
        contacts.add(contact);
        return contacts;
    }

    //метод будет удалять
    public Contacts without(ContactData contact) {
        Contacts contacts = new Contacts(this);
        contacts.remove(contact);
        return contacts;
    }
}
