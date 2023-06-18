package com.mauvaisetroupe.eadesignit.service.diagram.dto;

import org.springframework.util.StringUtils;

public class PositionAndSize {

    private Double x;
    private Double y;
    private Double width;
    private Double height;

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public PositionAndSize(Double x, Double y, Double width, Double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public PositionAndSize(String _x, String _y, String _width, String _height) {
        if (StringUtils.hasText(_x)) this.x = Double.parseDouble(_x);
        if (StringUtils.hasText(_y)) this.y = Double.parseDouble(_y);
        if (StringUtils.hasText(_width)) this.width = Double.parseDouble(_width);
        if (StringUtils.hasText(_height)) this.height = Double.parseDouble(_height);
    }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }
}
