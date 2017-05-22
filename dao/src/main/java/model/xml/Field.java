package model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Field {
    @XmlValue
    private String value;

    @XmlAttribute(name = "x")
    private int x;

    @XmlAttribute(name = "y")
    private int y;

    @XmlAttribute(name = "width")
    private int width;

    @XmlAttribute(name = "height")
    private int height;


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
