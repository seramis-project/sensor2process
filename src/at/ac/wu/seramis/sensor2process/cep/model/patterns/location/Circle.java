package at.ac.wu.seramis.sensor2process.cep.model.patterns.location;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by andreas on 11/18/16.
 */
@Root
public class Circle extends Shape {
    private static final long serialVersionUID = 8077843551202325533L;

    @Element
    private Point center;

    @Attribute
    private double radius;

    private transient javafx.scene.shape.Circle circle;

    public Circle(@Element(name = "center") Point center, @Attribute(name = "radius") double radius){
        this.center = center;
        this.radius = radius;
        this.circle = new javafx.scene.shape.Circle(center.getX(), center.getY(), radius);
    }

    @Override
    public boolean contains(Point point) {
        return circle.contains(point.getX(), point.getY());
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
