package at.ac.wu.seramis.sensor2process.cep.model.events;

/**
 * Created by andreas on 11/7/16.
 */
public class PositionEvent extends AbstractPositionEvent implements TemporalPosition {
    protected double certainty;

    @Override
    public double getCertainty() {
        return certainty;
    }
    @Override
    public void setCertainty(double certainty) {
        this.certainty = certainty;
    }

    public String toString() { return "{PositionEvent: id:"+id+", ts:"+timestamp+",\tx:"+x+",\ty:"+y+",\tz:"+z+" }"; }
}
