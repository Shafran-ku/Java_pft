package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PointTests {
    @Test
    public void testDistance() {
        Point p1 = new Point(15.0, 30.0);
        Point p2 = new Point(30.0, 50.0);
        Assert.assertEquals(p1.distance(p2), 25.0);
    }

    @Test
    public void testDistance2() {
        Point p1 = new Point(15.0, 20.0);
        Point p2 = new Point(30.0, 35.0);
        Assert.assertEquals(p1.distance(p2), 21.213203435596427);
    }

    @Test
    public void testDistance3() {
        Point p1 = new Point(10.0, 0.0);
        Point p2 = new Point(0.0, 100.0);
        Assert.assertEquals(p1.distance(p2), 100.4987562112089);
    }
}
