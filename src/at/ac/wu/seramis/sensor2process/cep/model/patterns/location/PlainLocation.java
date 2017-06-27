package at.ac.wu.seramis.sensor2process.cep.model.patterns.location;


import org.simpleframework.xml.*;

/**
 * Created by andreas on 11/17/16.
 */
@Root(name = "location")
public class PlainLocation implements ILocation, Named {
    private static final long serialVersionUID = -4921065352632186760L;

    @ElementUnion({
            @Element(name = "polygon", type=Polygon.class),
            @Element(name = "rectangle", type=Rectangle.class),
            @Element(name="circle", type=Circle.class)
    })
    protected Shape shape;

    @Attribute
    protected String name;

    public PlainLocation(Shape shape){
        this("unnamed", shape);
    }

    public PlainLocation(@Attribute(name="name") String name,
                         @ElementUnion({
                                 @Element(name = "polygon", type=Polygon.class),
                                 @Element(name = "rectangle", type=Rectangle.class),
                                 @Element(name = "circle", type=Circle.class)
                         }) Shape shape){
        this.shape = shape;
        this.name = name;
    }

    public Shape getShape() {
        return shape;
    }
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean contains(Point point) {
        return shape.contains(point);
    }
}
