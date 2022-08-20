package ru.stqa.pft.addressbook.model;

import com.google.common.collect.ForwardingSet;

import java.util.HashSet;
import java.util.Set;

public class Groups extends ForwardingSet<GroupData> {

    //объект, которому будет делегироваться
    private Set<GroupData> delegate;

    //берем множество из существующего объекта, который передан в качестве параметра,
    //строим новое множество, которое содержит те же самые элементы
    //и присваимваем это новое множество в качестве атрибута в новый создаваемый этим конструктором объект
    public Groups (Groups groups) {
        this.delegate = new HashSet<GroupData>(groups.delegate);
    }

    public Groups() {
        this.delegate = new HashSet<GroupData>();
    }


    @Override
    protected Set<GroupData> delegate() {
        return delegate;
    }

    public Groups withAdded(GroupData group) {
        Groups groups = new Groups(this);
        groups.add(group);
        return groups;
    }

    public Groups without(GroupData group) {
        Groups groups = new Groups(this);
        groups.remove(group);
        return groups;
    }

}
