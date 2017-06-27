package at.ac.wu.seramis.sensor2process.cep.model.patterns.location;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.awt.geom.Rectangle2D;

/**
 * Created by andreas on 11/18/16.
 */
@Root
public class Rectangle extends Shape {

    @Element
    protected Point topLeft;
    @Attribute
    protected double width;
    @Attribute
    protected double height;

    private transient Rectangle2D rect;


    public Rectangle(@Element(name="topLeft") Point topLeft, @Attribute(name = "width") double width, @Attribute(name = "height") double height){
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
        this.rect = new Rectangle2D.Double(topLeft.getX(),topLeft.getY(), width, height);
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public boolean contains(Point point) {
        return rect.contains(point.getX(), point.getY());
    }
}
