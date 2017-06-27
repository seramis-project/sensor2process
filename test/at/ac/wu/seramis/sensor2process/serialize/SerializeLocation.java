package at.ac.wu.seramis.sensor2process.serialize;

import at.ac.wu.seramis.sensor2process.cep.model.events.PositionBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.*;
import org.junit.Assert;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.InstantiationException;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.ValueRequiredException;

import java.io.File;

/**
 * Created by andreas on 11/18/16.
 */
public class SerializeLocation {
    @Test
    public void testSerializeComplexLocation() throws Exception {
        CompositeLocation location = new CompositeLocation("fitting room", CompositeLocation.Operator.OR,
//                new PlainLocation(new Polygon(new double[]{35,39.5,39.5,35}, new double[]{4,4,14,14},4)),  //35, 4, 39.5, 4, 39.5, 14, 35, 14)),
//                new PlainLocation(new Polygon(new double[]{35,39.5,39.5,35}, new double[]{35,35,45,45},4)),
//                new PlainLocation(new Polygon(new double[]{27.6,32.5,32.5,27.6}, new double[]{19,19,29,29},4)),
//                new PlainLocation(new Polygon(new double[]{20.2,25.2,25.2,20.2}, new double[]{3,3,14,14},4)),
//                new PlainLocation(new Polygon(new double[]{20.2,25.2,25.2,20.2}, new double[]{35,35,45,45},4)));
                new Rectangle(new Point(35,4),4.5,10),      // Polygon(35, 4, 39.5, 4, 39.5, 14, 35, 14),
                new Rectangle(new Point(35,35),5,10),       // Polygon(35, 35, 40, 35, 40, 45, 35, 45),
                new Rectangle(new Point(27.6, 19), 5, 10),  // Polygon(27.6, 19, 32.6, 19, 32.6, 29, 27.6, 29),
                new Rectangle(new Point(20.2, 3), 5, 10),   // Polygon(20.2, 3, 25.2, 3, 25.2, 13, 20.2, 13),
                new Rectangle(new Point(20.2, 35), 5, 10)); // Polygon(20.2, 35, 25.2, 35, 25.2, 45, 20.2, 45));

        Serializer serializer = new Persister();

        serializer.write(location, System.out);
    }

    @Test
    public void testPOS() throws Exception {
        ILocation loc = new PlainLocation("point of sale", new Rectangle(new Point(41.3, 44.6), 5, 3));
        Serializer serializer = new Persister();

        serializer.write(loc, System.out);
    }

    @Test
    public void testDeserializeComplexLocation() throws Exception {
        String xml = "<composite name=\"fitting room\" operator=\"OR\">\n" +
                "   <rectangle width=\"4.5\" height=\"10.0\">\n" +
                "      <topLeft>\n" +
                "         <x>35.0</x>\n" +
                "         <y>4.0</y>\n" +
                "      </topLeft>\n" +
                "   </rectangle>\n" +
                "   <polygon>\n" +
                "      <double>35.0</double>\n" +
                "      <double>35.0</double>\n" +
                "      <double>40.0</double>\n" +
                "      <double>35.0</double>\n" +
                "      <double>40.0</double>\n" +
                "      <double>45.0</double>\n" +
                "      <double>35.0</double>\n" +
                "      <double>45.0</double>\n" +
                "   </polygon>\n" +
                "   <polygon>\n" +
                "      <double>27.6</double>\n" +
                "      <double>19.0</double>\n" +
                "      <double>32.6</double>\n" +
                "      <double>19.0</double>\n" +
                "      <double>32.6</double>\n" +
                "      <double>29.0</double>\n" +
                "      <double>27.6</double>\n" +
                "      <double>29.0</double>\n" +
                "   </polygon>\n" +
                "   <polygon>\n" +
                "      <double>20.2</double>\n" +
                "      <double>3.0</double>\n" +
                "      <double>25.2</double>\n" +
                "      <double>3.0</double>\n" +
                "      <double>25.2</double>\n" +
                "      <double>13.0</double>\n" +
                "      <double>20.2</double>\n" +
                "      <double>13.0</double>\n" +
                "   </polygon>\n" +
                "   <polygon>\n" +
                "      <double>20.2</double>\n" +
                "      <double>35.0</double>\n" +
                "      <double>25.2</double>\n" +
                "      <double>35.0</double>\n" +
                "      <double>25.2</double>\n" +
                "      <double>45.0</double>\n" +
                "      <double>20.2</double>\n" +
                "      <double>45.0</double>\n" +
                "   </polygon>\n" +
                "</composite>";
        Serializer serializer = new Persister();
        CompositeLocation location = serializer.read(CompositeLocation.class, xml);
        Assert.assertTrue(location.contains(new Point(20.5, 36)));
        Assert.assertTrue(location.contains(new Point(36, 5)));
        Assert.assertFalse(location.contains(new Point(19, 36)));
    }

    @Test
    public void testComposite() throws Exception {
        CompositeLocation location = new CompositeLocation("cutCircle", CompositeLocation.Operator.AND,
                new Rectangle(new Point(35,4),4.5,10),
                new Circle(new Point(34,5), 5));

        CompositeLocation orLoc = new CompositeLocation("test", CompositeLocation.Operator.OR,
                location,
                new Rectangle(new Point(10,3),4,4));

        Serializer serializer = new Persister();

        serializer.write(orLoc, System.out);
    }

    @Test
    public void testLoadComposite() throws Exception {
        String xml = "<composite name=\"test\" operator=\"OR\">\n" +
                "   <composite name=\"cutCircle\" operator=\"AND\">\n" +
                "      <rectangle width=\"4.5\" height=\"10.0\">\n" +
                "         <topLeft>\n" +
                "            <x>35.0</x>\n" +
                "            <y>4.0</y>\n" +
                "         </topLeft>\n" +
                "      </rectangle>\n" +
                "      <circle radius=\"5.0\">\n" +
                "         <center>\n" +
                "            <x>34.0</x>\n" +
                "            <y>5.0</y>\n" +
                "         </center>\n" +
                "      </circle>\n" +
                "   </composite>\n" +
                "   <rectangle width=\"4.0\" height=\"4.0\">\n" +
                "      <topLeft>\n" +
                "         <x>10.0</x>\n" +
                "         <y>3.0</y>\n" +
                "      </topLeft>\n" +
                "   </rectangle>\n" +
                "</composite>";
        Serializer serializer = new Persister();
        CompositeLocation location = serializer.read(CompositeLocation.class, xml);
        Assert.assertTrue(location.contains(new Point(11, 3)));
    }

    @Test
    public void testRegistry() throws Exception {
        String defaultValue = LocationRegistry.getInstance().getTypeOfLocationThatContains(new Point(0.0,0.0));
        Assert.assertEquals(PositionBusinessEvent.SALES_FLOOR_TYPE, defaultValue);

    }

    @Test
    public void testDeserialize() throws Exception {
        File[] files = new File("./resources/locations").listFiles();
        Serializer serializer = new Persister();
        for (File locFile : files){
            ILocation location;
            try{
                location = serializer.read(CompositeLocation.class, locFile);
            } catch (InstantiationException e){
                location = serializer.read(PlainLocation.class, locFile);
            } catch (ValueRequiredException e){
                location = serializer.read(PlainLocation.class, locFile);
            }

            System.out.println(location);
        }

    }
}
