package at.ac.wu.seramis.sensor2process;

import at.ac.wu.seramis.sensor2process.cep.CEPBusinessPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingTrivial;
import at.ac.wu.seramis.sensor2process.cep.model.events.PositionEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.MovingTrivialPattern;
import at.ac.wu.seramis.sensor2process.sinks.TestSink;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by andreas on 11/18/16.
 */
public class MovingTrivialTest {
    private static final String EPC = "epcTest1";

    @Test
    public void testTrivialMove() throws Exception {
        PositionEvent e1 = new PositionEvent();
        e1.setId(EPC);
        e1.setTimestamp(1477346400000l); // 10/25/2016 00:00:000
        e1.setCertainty(0.8);
        e1.setX(10);
        e1.setY(10);

        // moved 5 to the right
        PositionEvent e2 = new PositionEvent();
        e2.setId(EPC);
        e2.setTimestamp(1477350000000l); // 10/25/2016 01:00:000
        e2.setCertainty(0.8);
        e2.setX(15);
        e2.setY(10);

        // moved 10 up, 5 minutes later
        PositionEvent e3 = new PositionEvent();
        e3.setId(EPC);
        e3.setTimestamp(1477350300000l); // 10/25/2016 01:05:000
        e3.setCertainty(0.8);
        e3.setX(15);
        e3.setY(0);

        // moved 1 down and 1 left, 5 minutes later
        PositionEvent e4 = new PositionEvent();
        e4.setId(EPC);
        e4.setTimestamp(1477350600000l); // 10/25/2016 01:05:000
        e4.setCertainty(0.8);
        e4.setX(14);
        e4.setY(1);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<TemporalPosition> dataStream = env.fromElements(e1,e2,e3, e4).map(e -> (TemporalPosition)e).assignTimestampsAndWatermarks(new AscendingTimestampExtractor<TemporalPosition>()
        {
            @Override
            public long extractAscendingTimestamp(TemporalPosition temporalSenseEvent)
            {
                return temporalSenseEvent.getTimestamp();
            }
        });
        DataStream<IBusinessEvent> movingTrivial = CEPBusinessPosition.toBusinessEventsStream(dataStream, new MovingTrivialPattern()).map(e -> (IBusinessEvent)e);

        movingTrivial.print();
        movingTrivial.addSink(new TestSink());
        try
        {
            env.execute("Simulated DataStream");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Assert.assertEquals(3, TestSink.testObjects.size());
        Assert.assertTrue(((MovingTrivial)TestSink.testObjects.get(0)).isMoving());
        Assert.assertTrue(((MovingTrivial)TestSink.testObjects.get(1)).isMoving());
        Assert.assertFalse(((MovingTrivial)TestSink.testObjects.get(2)).isMoving());

    }
}
