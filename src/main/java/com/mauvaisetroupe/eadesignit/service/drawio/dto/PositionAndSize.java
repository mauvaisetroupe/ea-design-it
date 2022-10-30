package com.mauvaisetroupe.eadesignit.service.drawio.dto;

public class PositionAndSize {

    private String x;
    private String y;
    private String width;
    private String height;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public PositionAndSize(String x, String y, String width, String height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
