package at.ac.wu.seramis.sensor2process.cep.model.events;

/**
 * Created by andreas on 11/9/16.
 */
public class SenseEvent extends AbstractPositionEvent implements TemporalSenseEvent{
    protected double signalStrength;

    @Override
    public double getSignalStrength() {
        return signalStrength;
    }
    public void setSignalStrength(double signalStrength) {
        this.signalStrength = signalStrength;
    }
}
