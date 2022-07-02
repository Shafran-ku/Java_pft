package ru.stqa.pft.sandbox;

public class Point {

    public double x, y;    //объявлены переменные-координаты точек

    //конструктор
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //ф-ия main, принимает на вход массив параметров
    public static void main(String[] args) {
        Point p1 = new Point(10.0, 20.0);     //создаем объект p1 -экземпляр класса Point (x1,y1)
        Point p2 = new Point(30.0,50.0);     //создаем объект p2 -экземпляр класса Point (x2,y2)

        System.out.println("Расстояние между точками c координатами ("  + p1.x + ", " + p1.y + ")"
                + " и " + "(" + p2.x + ", " + p2.y + ")" + " равно " + p1.distance(p2));
    }

    //ф-ия distance принимает на вход 2 параметра-координаты точек(объекты класса Point)
    //и возвращает вычисленное расстояние
    public double distance(Point p1) {
        return Math.sqrt((this.x - p1.x)*(this.x - p1.x) + (this.y - p1.y)*(this.y -p1.y));
    }

}
