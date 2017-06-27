package at.ac.wu.seramis.sensor2process.cep.model.events.factory;

import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.factory.PositionEventFactory;

/**
 * Created by andreas on 11/7/16.
 */
public abstract class TemporalPositionFactory<P extends TemporalPosition> {

    public static final TemporalPositionFactory POSITION_EVENT_FACTORY = new PositionEventFactory();
//    public static final TemporalPositionFactory EPC_FACTORY = new EPCFactory();

    public abstract P create();
}