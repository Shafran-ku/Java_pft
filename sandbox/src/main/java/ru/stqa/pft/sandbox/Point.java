package ru.stqa.pft.sandbox;

public class Point {

    public double x1, x2, y1, y2;    //объявлены переменные-координаты точек

    //ф-ия main, принимает на вход массив параметров
    public static void main(String[] args) {
        Point p1 = new Point();     //создаем объект p1 -экземпляр класса Point

        //установка значений атрибутов объекта p1
        p1.x1 = 5;
        p1.y1 = 10;

        Point p2 = new Point();     //создаем объект p2 -экземпляр класса Point

        //установка значений атрибутов объекта p2
        p2.x2 = 20;
        p2.y2 = 30;

        System.out.println("Расстояние между точками ("  + p1.x1 + ", " + p1.y1 + ")"
                + " и " + "(" + p2.x2 + ", " + p2.y2 + ")" + " = " + distance(p1, p2));
    }

    //ф-ия distance принимает на вход 2 параметра-координаты точек(объекты класса Point)
    //и возвращает вычисленное расстояние
    public static double distance(Point p1, Point p2) {
        return Math.sqrt((p2.x2 - p1.x1)*(p2.x2 - p1.x1) + (p2.y2 - p1.y1)*(p2.y2 - p1.y1));
    }

}
