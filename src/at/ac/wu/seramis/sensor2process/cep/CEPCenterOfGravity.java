package at.ac.wu.seramis.sensor2process.cep;

import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.processor.LocationProcessor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * Created by andreas on 11/9/16.
 *
 * Extracts Positions from a stream of sense events.
 * Groups events by their id (TemporalSenseEvent#getId()).
 */
public class CEPCenterOfGravity
{

	public static final int SECONDS_GAP = 10;

	/**
	 * Creates a new DataStream from the temporal sense events
	 *
	 * @param dataStream
	 *            DataStream of type TemporalSenseEvent
	 * @return DataStream of type TemporalPosition
	 */
	public static DataStream<TemporalPosition> extractPositionsFromSensors(DataStream<TemporalSenseEvent> dataStream)
	{
		return extractPositionsFromSensors(dataStream, SECONDS_GAP);
	}

	public static DataStream<TemporalPosition> extractPositionsFromSensors(DataStream<TemporalSenseEvent> dataStream, int secondsGap)
	{
		return dataStream.keyBy(tse -> tse.getId()).window(ProcessingTimeSessionWindows.withGap(Time.seconds(secondsGap))).apply(new LocationProcessor());
	}

	public static DataStream<TemporalPosition> extractPositionsFromSensorsEventTime(DataStream<TemporalSenseEvent> dataStream, int secondsGap)
	{
		return dataStream.keyBy(tse -> tse.getId()).window(EventTimeSessionWindows.withGap(Time.seconds(secondsGap))).apply(new LocationProcessor());
	}

	public static DataStream<TemporalPosition> extractPositionsFromSensorsFixedWindowEventTime(DataStream<TemporalSenseEvent> dataStream, int windowSizeSeconds)
	{
		return dataStream.keyBy(tse -> tse.getId()).timeWindow(Time.seconds(windowSizeSeconds)).apply(new LocationProcessor());
	}

    public static DataStream<TemporalPosition> extractPositionsFromSensorsSlidingindowEventTime(DataStream<TemporalSenseEvent> dataStream, int windowSizeSeconds, int slideSeconds) {
		return dataStream.keyBy(tse -> tse.getId()).window(SlidingEventTimeWindows.of(Time.seconds(windowSizeSeconds), Time.seconds(slideSeconds))).apply(new LocationProcessor());
    }
}
