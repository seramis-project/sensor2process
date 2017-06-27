package at.ac.wu.seramis.sensor2process.cep.model.patterns.location;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by andreas on 11/18/16.
 */
@Root
public class Point implements Serializable{
    private static final long serialVersionUID = 3401249820446751962L;

    @Element
    private final double x;

    @Element
    private final double y;

    public Point(@Element(name="x") double x, @Element(name="y") double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
