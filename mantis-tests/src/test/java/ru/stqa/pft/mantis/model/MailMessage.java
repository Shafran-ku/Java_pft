package ru.stqa.pft.mantis.model;

public class MailMessage {

    public String to;   //кому пришло письмо
    public String text; //текст письма

    public MailMessage(String to, String text) {        //(String s, String content)
        this.to = to;
        this.text = text;
    }
}