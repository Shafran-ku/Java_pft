package ru.stqa.pft.sandbox;

public class Square {
  public double l;

  public Square(double l) {               //конструктор
    this.l = l;       //присваивание переданного значения в атрибут объекта
  }

  public double area() {
    return this.l * this.l;
  }
}


