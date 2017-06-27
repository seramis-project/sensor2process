package at.ac.wu.seramis.sensor2process.cep.model.events;

import at.ac.wu.seramis.sensor2process.cep.pgsql.model.BusinessEvent;


/**
 * Created by andreas on 11/17/16.
 */
public class PositionBusinessEvent extends AbstractBusinessEvent {

    public static final String FITTING_ROOM_TYPE = "fitting room";
    public static final String POINT_OF_SALES_TYPE = "point of sale";
    public static final String SALES_FLOOR_TYPE = "sales floor";  // catch all others!

    protected TemporalPosition tag;

    protected String type;

    protected long startTimestamp;
    protected long endTimestamp;

    public PositionBusinessEvent(BusinessEvent businessEvent, String type)
    {
        super(businessEvent.getEpc());
//        JSONObject obj = new JSONObject(businessEvent.getInfo());
        this.startTimestamp = businessEvent.getStartTimestamp();
        this.endTimestamp = businessEvent.getEndTimestamp();

//        TemporalPosition tag =
//
//        TemporalPosition tag =
//        TemporaPosition tag = new TE(obj.getDouble("x"), obj.getDouble("y"));
//        tag.setActor(businessEvent.getEpc());
//        tag.setTime_millis(businessEvent.getTimestamp());
//        this.tag = businessEvent.;
//        this.type = type;
    }

    public PositionBusinessEvent(TemporalPosition tag, String type)
    {
        super(tag.getId());
        this.tag = tag;
        this.type = type;
        this.startTimestamp = tag.getTimestamp();
        this.endTimestamp = tag.getTimestamp();
    }

    public PositionBusinessEvent(TemporalPosition position){
        super(position.getId());
    }

    public PositionBusinessEvent(TemporalPosition tag, String type, long startTimeStamp, long endTimeStamp) {
        super(tag.getId());
        this.tag = tag;
        this.type = type;
        this.startTimestamp = startTimeStamp;
        this.endTimestamp = endTimeStamp;
    }

    public TemporalPosition getTag()
    {
        return this.tag;
    }

    @Override
    public String toString()
    {
        return getType() + " business event  " + this.tag.getId() + "  / " + this.tag.getTimestamp() + " / " + " X: " + this.tag.getX() + " Y: " + this.tag.getY();
    }

    @Override
    public String getType() {
        return type;
    }

	@Override
	public long getStartTimestamp() {
		return startTimestamp;
	}

	@Override
	public long getEndTimestamp() {
		return endTimestamp;
	}

	@Override
	public String getInfo() {
		return "{" + "\"x\": " + tag.getX() +
                ",\"y\": " + tag.getY() +
                ",\"z\": "+ tag.getZ()+
                ", \"type\": "+getType() +
                ", \"timestamp\": "+getStartTimestamp() + "}";
	}
}
