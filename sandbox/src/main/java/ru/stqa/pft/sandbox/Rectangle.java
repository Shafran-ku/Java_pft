package ru.stqa.pft.sandbox;

public class Rectangle {
    public double a;
    public double b;

    public Rectangle(double a, double b) {
        this.a = a;         //присваивание переданного значения в атрибут объекта
        this.b = b;         //присваивание ..
    }

    public double area() {
        return this.a * this.b;
    }
}


