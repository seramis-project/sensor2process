package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.CompositeLocation;
import at.ac.wu.seramis.sensor2process.cep.model.events.PositionBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.PlainLocation;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.Polygon;

public class FittingRoomPattern extends LocationBusinessEventPattern
{
	private static final long serialVersionUID = -5009128714025320254L;

	// using "real" coordinate system
	//	private static final Polygon fitting1 = new Polygon(35, 4, 39.5, 4, 39.5, 14, 35, 14);
	//	private static final Polygon fitting2 = new Polygon(35, 35, 39.5, 35, 39.5, 45, 35, 45);
	//
	//	private static final Polygon fitting3 = new Polygon(28, 19, 32.5, 19, 32.5, 29, 28, 29);
	//
	//	private static final Polygon fitting4 = new Polygon(20.5, 3, 25, 3, 25, 14, 20.5, 14);
	//	private static final Polygon fitting5 = new Polygon(20.5, 35, 26, 35, 26, 45, 20.5, 45);

	public FittingRoomPattern()
	{
		super(new CompositeLocation(PositionBusinessEvent.FITTING_ROOM_TYPE, CompositeLocation.Operator.OR,
//				new PlainLocation(new Polygon2D(new float[]{35,39.5f,39.5f,35}, new float[]{4,4,14,14},4)),  //35, 4, 39.5, 4, 39.5, 14, 35, 14)),
//				new PlainLocation(new Polygon2D(new float[]{35,39.5f,39.5f,35}, new float[]{35,35,45,45}, 4)),
//				new PlainLocation(new Polygon2D(new float[]{27.6f,32.5f,32.5f,27.6f}, new float[]{19,19,29,29}, 4)),
//				new PlainLocation(new Polygon2D(new float[]{20.2f,25.2f,25.2f,20.2f}, new float[]{3,3,14,14}, 4)),
//				new PlainLocation(new Polygon2D(new float[]{20.2f,25.2f,25.2f,20.2f}, new float[]{35,35,45,45}, 4))));
//				new PlainLocation(new Polygon(new double[]{35,39.5,39.5,35}, new double[]{4,4,14,14}, 4)),  //35, 4, 39.5, 4, 39.5, 14, 35, 14)),
//				new PlainLocation(new Polygon(new double[]{35,39.5,39.5,35}, new double[]{35,35,45,45}, 4)),
//				new PlainLocation(new Polygon(new double[]{27.6,32.5,32.5,27.6}, new double[]{19,19,29,29},4)),
//				new PlainLocation(new Polygon(new double[]{20.2,25.2,25.2,20.2}, new double[]{3,3,14,14},4)),
//				new PlainLocation(new Polygon(new double[]{20.2,25.2,25.2,20.2}, new double[]{35,35,45,45},4))));
				new PlainLocation("fitting room1", new Polygon(35, 4, 39.5, 4, 39.5, 14, 35, 14)),
				new PlainLocation("fitting room2", new Polygon(35, 35, 40, 35, 40, 45, 35, 45)),
                new PlainLocation("fitting room3", new Polygon(27.6, 19, 32.6, 19, 32.6, 29, 27.6, 29)),
                new PlainLocation("fitting room4", new Polygon(20.2, 3, 25.2, 3, 25.2, 13, 20.2, 13)),
                new PlainLocation("fitting room5", new Polygon(20.2, 35, 25.2, 35, 25.2, 45, 20.2, 45))), PositionBusinessEvent.FITTING_ROOM_TYPE);
	}
}
