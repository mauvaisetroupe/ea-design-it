package com.mauvaisetroupe.eadesignit.service.drawio.dto;

public class Point {

    private String x;
    private String y;

    public Point(String x, String y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }
}
