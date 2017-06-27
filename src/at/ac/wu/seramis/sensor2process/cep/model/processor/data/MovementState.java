package at.ac.wu.seramis.sensor2process.cep.model.processor.data;


import at.ac.wu.seramis.sensor2process.cep.model.events.PositionBusinessEvent;

import java.util.List;

/**
 * Created by andreas on 11/24/16.
 */
public class MovementState {
    private List<PositionBusinessEvent> lastPositions;
    private boolean moving;

    public List<PositionBusinessEvent> getLastPositions() {
        return lastPositions;
    }

    public void setLastPositions(List<PositionBusinessEvent> lastPositions) {
        this.lastPositions = lastPositions;
    }

    public boolean isMoving() {
        return moving;
    }
    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
