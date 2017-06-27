package at.ac.wu.seramis.sensor2process.cep.model.patterns.location;

import org.simpleframework.xml.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by andreas on 11/17/16.
 */
@Root(name = "composite")
public class CompositeLocation implements ILocation, Named {
    private static final long serialVersionUID = -7051758909419816956L;

    public enum Operator{
        OR, AND, NOT
    }

    @Attribute(required = false)
    protected String name;

    @Attribute
    protected Operator operator;

    @ElementListUnion({
            @ElementList(entry="polygon", inline=true, type=Polygon.class),
            @ElementList(entry="rectangle", inline=true, type=Rectangle.class),
            @ElementList(entry="circle", inline=true, type=Circle.class),
            @ElementList(entry="composite", inline=true, type=CompositeLocation.class)
    })
    protected List<ILocation> locations;

    public CompositeLocation(){}

    public CompositeLocation(@Attribute(name = "name") String name, @Attribute(name = "operator") Operator operator, ILocation...locations){
        this.name = name;
        this.operator = operator;
        this.locations = Arrays.asList(locations);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public List<ILocation> getLocations() {
        return locations;
    }

    public void setLocations(List<ILocation> locations) {
        this.locations = locations;
    }

    @Override
    public boolean contains(Point point) {
        boolean contains = false;
        switch (operator){
            case OR:
            case NOT:
                for (ILocation loc : locations){
                    contains |= loc.contains(point);
                }
                break;
            case AND:
                contains = true;
                for (ILocation loc : locations){
                    contains &= loc.contains(point);
                }
                break;
        }
        if (Operator.NOT.equals(operator)){
            contains = !contains;
        }
        return contains;
    }

}
