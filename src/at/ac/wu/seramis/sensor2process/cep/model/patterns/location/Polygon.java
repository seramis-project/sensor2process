package at.ac.wu.seramis.sensor2process.cep.model.patterns.location;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import org.apache.batik.ext.awt.geom.Polygon2D;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by andreas on 11/18/16.
 */
@Root(name = "polygon")
public class Polygon extends Shape {
    private static final long serialVersionUID = 4991427717627081426L;

    @ElementList(inline=true)
    private List<Double> coordinates;

    private transient Polygon2D polygon;

    public Polygon(@ElementList(inline=true) List<Double> coordinates){
        this.coordinates = coordinates;
        Iterator<Double> iter = coordinates.iterator();
        List<Double> xPoints = new ArrayList<>();
        List<Double> yPoints = new ArrayList<>();
        while(iter.hasNext()){
            xPoints.add(iter.next());
            yPoints.add(iter.next());
        }
        float[] xPointsF = Floats.toArray(xPoints);
        float[] yPointsF = Floats.toArray(yPoints);
        this.polygon = new Polygon2D(xPointsF, yPointsF, xPoints.size());
    }

    public Polygon(double...coordinates){
        this(Doubles.asList(coordinates));
    }

//    public Polygon(@ElementList(inline=true, name="xPoints") List<Double> xPoints, @ElementList(inline=true, name="yPoints") List<Double> yPoints){
//        this.xPoints = xPoints;
//        this.yPoints = yPoints;
//
//        float[] xPointsF = Floats.toArray(xPoints);
//        float[] yPointsF = Floats.toArray(yPoints);
//
//        this.polygon = new Polygon2D(xPointsF, yPointsF, xPoints.size());
//    }

//    public Polygon(double[] xPointsArray, double[] yPointsArray, int points) {
//        this(Doubles.asList(xPointsArray), Doubles.asList(yPointsArray));
//    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean contains(Point point) {
        return polygon.contains(point.getX(), point.getY());
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "coordinates=" + coordinates +
                '}';
    }
}
