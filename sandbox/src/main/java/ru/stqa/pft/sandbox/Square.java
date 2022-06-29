package ru.stqa.pft.sandbox;

public class Square {
  public double l;

  //конструктор;
  //присваивание переданного значения в атрибут объекта
  public Square(double l) {
    this.l = l;
  }

  public double area() {
    return this.l * this.l;
  }
}


