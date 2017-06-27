package at.ac.wu.seramis.sensor2process.cep.model.processor;

import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.factory.TemporalPositionFactory;
import at.ac.wu.seramis.sensor2process.utils.CenterOfGravity;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;


/**
 * Created by andreas on 11/7/16.
 */
public class LocationProcessor extends RichWindowFunction<TemporalSenseEvent, TemporalPosition, String, TimeWindow> {

    /**
     * The ValueState handle.
     */
    private transient ValueState<TemporalPosition> lastKnownEvent;

    @Override
    public void open(Configuration config) {
        ValueStateDescriptor<TemporalPosition> descriptor =
                new ValueStateDescriptor<>(
                        "lastKnownEvent", // the state name
                        TypeInformation.of(new TypeHint<TemporalPosition>() {}), // type information
                        null); // default value of the state, if nothing was set
        lastKnownEvent = getRuntimeContext().getState(descriptor);
    }

    @Override
    public void apply(String s, TimeWindow window, Iterable<TemporalSenseEvent> events, Collector<TemporalPosition> collector) throws Exception {
        // access the state value
        TemporalPosition lastEvent = lastKnownEvent.value();

        TemporalPosition position = CenterOfGravity.getCenterOfGravityWithPrevious(events, lastEvent, TemporalPositionFactory.POSITION_EVENT_FACTORY);

        lastKnownEvent.update(position);
        collector.collect(position);
    }
}
