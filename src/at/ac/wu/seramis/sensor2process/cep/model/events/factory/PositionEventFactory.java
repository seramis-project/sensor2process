package at.ac.wu.seramis.sensor2process.cep.model.events.factory;

import at.ac.wu.seramis.sensor2process.cep.model.events.PositionEvent;

/**
 * Created by andreas on 11/7/16.
 */
public class PositionEventFactory extends TemporalPositionFactory<PositionEvent> {
    @Override
    public PositionEvent create() {
        return new PositionEvent();
    }
}
