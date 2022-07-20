package ru.stqa.pft.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Collections {

    public static void main(String[] args) {
        //объявление массива
        String[] langs = {"Java", "C#", "Python", "PHP"};

        //Arrays.asList - преобразование массива в список
        List<String> languages = Arrays.asList("Java", "C#", "Python", "PHP");

        //переменная l - ссылка на каждый элемент массива
        for (String l : languages) {
            System.out.println("Я хочу выучить " + l);
        }
    }
}
