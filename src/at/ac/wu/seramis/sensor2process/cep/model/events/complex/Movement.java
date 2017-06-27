package at.ac.wu.seramis.sensor2process.cep.model.events.complex;

import at.ac.wu.seramis.sensor2process.cep.model.events.AbstractBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.PositionBusinessEvent;

import java.util.List;

/**
 * Created by andreas on 11/24/16.
 */
public class Movement extends AbstractBusinessEvent {

    public static final String TYPE = "movement";

    private long startTime, endTime;

    private List<PositionBusinessEvent> path;

    public Movement(List<PositionBusinessEvent> path){
        super(path.get(0).getId());
        this.path = path;
        this.startTime = path.get(0).getStartTimestamp();
        this.endTime = path.get(path.size()-1).getEndTimestamp();
    }


    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public long getStartTimestamp() {
        return startTime;
    }

    @Override
    public long getEndTimestamp() {
        return endTime;
    }

    @Override
    public String getInfo() {
        String returnVal = "{ \"path\": [ ";
        boolean firstVal = true;
        for (PositionBusinessEvent pos : path){
            if (!firstVal){
                returnVal += ", ";
            } else {
                firstVal = false;
            }
            returnVal += pos.getInfo();
//            returnVal += "{ \"x\": "+pos.getTag().getX();
//            returnVal += ", \"y\": "+pos.getTag().getY();
//            returnVal += ", \"z\": "+pos.getTag().getZ();
//            returnVal += ", \"time\": "+pos.getTag().getTimestamp();
//            returnVal += "}";
        }
        returnVal += " ] }";
        return returnVal;
    }
}
