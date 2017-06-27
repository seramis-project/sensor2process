package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import at.ac.wu.seramis.sensor2process.cep.model.events.PositionBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.PlainLocation;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.Point;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.Rectangle;


/**
 * Created by andreas on 11/17/16.
 */
public class PointOfSalesPattern extends LocationBusinessEventPattern {

    private static final long serialVersionUID = 5671995183459644744L;

    public PointOfSalesPattern(){
        super(
//                new PlainLocation(new Polygon2D(new float[]{41.286462228870604f, 46.49214659685864f, 46.49214659685864f, 41.286462228870604f},
//                new float[]{44.666666666666664f, 44.666666666666664f, 47.333333333333336f,47.333333333333336f},4)));
//        new PlainLocation(new Polygon(new double[]{41.286462228870604, 46.49214659685864, 46.49214659685864, 41.286462228870604},
//                new double[]{44.666666666666664, 44.666666666666664, 47.333333333333336, 47.333333333333336},4)));
           new PlainLocation(new Rectangle(new Point(41.3, 44.6), 5, 3)), PositionBusinessEvent.POINT_OF_SALES_TYPE);
//           new PlainLocation(new Polygon(41.286462228870604, 44.666666666666664, 46.49214659685864, 44.666666666666664, 46.49214659685864, 47.333333333333336, 41.286462228870604, 47.333333333333336)));
    }
}
