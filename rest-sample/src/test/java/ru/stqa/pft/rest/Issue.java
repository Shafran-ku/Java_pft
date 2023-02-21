package ru.stqa.pft.rest;

import java.util.Objects;

public class Issue {

    private int id;
    private String subject;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return Objects.equals(state_name, issue.state_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state_name);
    }

    private Object state_name;


    public int getId() {
        return id;
    }

    public Issue withId(int id) {
        this.id = id;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Issue withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Issue withDescription(String description) {
        this.description = description;
        return this;
    }

    //статус issue
    public Object getState_name() {
        return state_name;
    }
}
