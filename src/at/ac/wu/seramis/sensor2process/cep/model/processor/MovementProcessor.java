package at.ac.wu.seramis.sensor2process.cep.model.processor;

import at.ac.wu.seramis.sensor2process.cep.model.events.*;
import at.ac.wu.seramis.sensor2process.cep.model.events.complex.Movement;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.MovingTrivialPattern;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.LocationRegistry;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.Point;
import at.ac.wu.seramis.sensor2process.cep.model.processor.data.MovementState;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Assumes a window size of size 2.
 *
 * Created by andreas on 11/24/16.
 */
public class MovementProcessor extends RichWindowFunction<TemporalPosition, IBusinessEvent, String, GlobalWindow> {

    private static final long serialVersionUID = -4360801821927116673L;
    /**
     * The ValueState handle. It stores the last {@link MovementState}
     */
    private transient ValueState<MovementState> lastMoving;

    @Override
    public void open(Configuration config) {
        ValueStateDescriptor<MovementState> descriptor =
                new ValueStateDescriptor<>(
                        "lastMoving", // the state name
                        TypeInformation.of(new TypeHint<MovementState>() {}), // type information
                        null); // default value of the state, if nothing was set
        lastMoving = getRuntimeContext().getState(descriptor);
    }

    @Override
    public void apply(String s, GlobalWindow window, Iterable<TemporalPosition> input, Collector<IBusinessEvent> out) throws Exception {
        // assume a window of size two
        Iterator<TemporalPosition> iter = input.iterator();
        TemporalPosition from = iter.next();
        TemporalPosition to = iter.next();
        assert ! iter.hasNext();

        boolean moved = MovingTrivialPattern.getMoved(from,to);

        // access the state value
        MovementState lastState = lastMoving.value();
        if (lastState == null){
            lastState = new MovementState();

            lastState.setMoving(moved);
            lastState.setLastPositions(new ArrayList<>());

            PositionBusinessEvent positionEvent = getPositionBusinessEvent(from);
            if (!moved) {
                out.collect(positionEvent);
            }
            lastState.getLastPositions().add(positionEvent);
        }

        // The origin is the place where the item last was standing
        TemporalPosition origin = from;
        if (!lastState.isMoving()){
            // fix the origin to be the first event of the non-moving areas
            origin = lastState.getLastPositions().get(0).getTag();
        }
        // did we move away from the last standing or moving position?
        moved = MovingTrivialPattern.getMoved(origin,to);

        if (lastState.isMoving()){
            if (!moved) {
                // stopped Moving
                Movement movement = new Movement(lastState.getLastPositions());
                out.collect(movement);
//                StopsMoving stopsMoving = new StopsMoving(later);
//                out.collect(stopsMoving);
                lastState.getLastPositions().clear();
            } else {
                // still moving: ignore, only add the next station along the path
            }
            lastState.getLastPositions().add(getPositionBusinessEvent(to));
        } else {
            if (moved){
                // started Moving

                // store the staying event until the from timestamp
                out.collect(getPositionBusinessEvent(origin, from.getTimestamp()));

//                StartsMoving startsMoving = new StartsMoving(from);
//                out.collect(startsMoving);

                lastState.getLastPositions().clear();

                // "from" location was still within the threshold of the non-moving location
                lastState.getLastPositions().add(getPositionBusinessEvent(from));
                lastState.getLastPositions().add(getPositionBusinessEvent(to));
            } else {
                // not moved and not moving: ignore
            }
        }
        lastState.setMoving(moved);
        lastMoving.update(lastState);
    }

    private PositionBusinessEvent getPositionBusinessEvent(TemporalPosition position) {
        return getPositionBusinessEvent(position, position.getTimestamp());
    }
    private PositionBusinessEvent getPositionBusinessEvent(TemporalPosition position, long endTimestamp) {
        return new PositionBusinessEvent(position,
                LocationRegistry.getInstance().getTypeOfLocationThatContains(new Point(position.getX(), position.getY())),
                position.getTimestamp(), endTimestamp);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
}
