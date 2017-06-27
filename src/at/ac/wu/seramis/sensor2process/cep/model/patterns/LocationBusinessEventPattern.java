package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import at.ac.wu.seramis.sensor2process.cep.model.events.PositionBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.ILocation;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.Point;
import org.apache.flink.cep.pattern.Pattern;
import java.util.Map;

/**
 * Created by andreas on 11/17/16.
 */
public class LocationBusinessEventPattern extends AbstractBusinessEventPattern<TemporalPosition> {

    private static final long serialVersionUID = 936037204398005914L;
    /**
     * the pattern code / name
     */
    protected final String codeName;
    /**
     * The location definition that is used to query for containment
     */
    protected ILocation location;


    public LocationBusinessEventPattern(ILocation location, String codeName) {
        this.location = location;
        this.codeName = codeName;
    }

    @Override
    public PositionBusinessEvent create(Map<String, TemporalPosition> pattern) {
        return new PositionBusinessEvent(pattern.get("location"), codeName);
    }

    @Override
    public Pattern<TemporalPosition, TemporalPosition> getEventPattern() {
        return Pattern.<TemporalPosition>begin("prev").subtype(TemporalPosition.class).where(evt ->
                {  // make sure that previously the item was somewhere else.
                    Point evtLocation = new Point(evt.getX(), evt.getY());
                    return !location.contains(evtLocation);
                }
            ).next("location").subtype(TemporalPosition.class).where(evt ->
                {
                    Point evtLocation = new Point(evt.getX(), evt.getY());
                    return location.contains(evtLocation);
                }
            );
    }

    @Override
    public String getPatternCode() {
        return codeName;
    }
}
