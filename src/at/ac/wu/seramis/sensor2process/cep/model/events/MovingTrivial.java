package at.ac.wu.seramis.sensor2process.cep.model.events;

import at.ac.wu.seramis.sensor2process.cep.pgsql.model.BusinessEvent;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.json.JSONObject;

import at.ac.wu.seramis.sensor2process.database.model.Tag;

public class MovingTrivial extends AbstractBusinessEvent
{
	public static final String TYPE = "moving_trivial";

	private boolean moving;
	private TemporalPosition fromTag;
	private TemporalPosition toTag;
	private final DistanceMeasure dist = new EuclideanDistance();
	
	private double quotaFrom = -1, quotaTo = -1;

	public MovingTrivial(BusinessEvent businessEvent)
	{
		super(businessEvent.getEpc());
		JSONObject obj = new JSONObject(businessEvent.getInfo());

		TemporalPosition toTag = new Tag(obj.getDouble("to_x"), obj.getDouble("to_y"));
		TemporalPosition fromTag = new Tag(obj.getDouble("from_x"), obj.getDouble("from_y"));
		toTag.setId(businessEvent.getEpc());
		fromTag.setId(businessEvent.getEpc());
		fromTag.setTimestamp(businessEvent.getTimestamp());
		toTag.setTimestamp(businessEvent.getTimestamp());
		this.toTag = toTag;
		this.fromTag = fromTag;

	}

	public MovingTrivial(TemporalPosition fromTag, TemporalPosition toTag)
	{
		this(fromTag, toTag, true);
	}

	public MovingTrivial(TemporalPosition fromTag, TemporalPosition toTag, boolean moving) {
		super(fromTag.getId());
		this.fromTag = fromTag;
		this.toTag = toTag;
		this.moving = moving;
	}

	public TemporalPosition getFrom()
	{
		return this.fromTag;
	}

	public void setFrom(TemporalPosition fromTag)
	{
		this.fromTag = fromTag;
	}

	public TemporalPosition getTo()
	{
		return this.toTag;
	}

	public void setTo(TemporalPosition toTag)
	{
		this.toTag = toTag;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public void setQuotaFrom(double quotaFrom)
	{
		this.quotaFrom = quotaFrom;
	}
	
	public void setQuotaTo(double quotaTo)
	{
		this.quotaTo = quotaTo;
	}
	
	public double getQuotaFrom()
	{
		return this.quotaFrom;
	}
	
	public double getQuotaTo()
	{
		return this.quotaTo;
	}
	
	@Override
	public String getType()
	{
		return TYPE;
	}

	@Override
	public String toString()
	{
		return TYPE+" [moving: "+moving+", Initial: " + (this.fromTag!=null?this.fromTag.getX()+"/"+this.fromTag.getY():"null") + "/" +
				" Final: " + (this.toTag!=null?this.toTag.getX()+"/"+this.toTag.getY():"null") + " d:" + this.getDistance();
	}

	public double getDistance()
	{
		//	return dist.compute(new double[]{fromTag.getX(), fromTag.getY()}, new double[]{toTag.getX(), toTag.getY()});
		return dist.compute(new double[]{fromTag.getX(), fromTag.getY(), fromTag.getZ()}, new double[]{toTag.getX(), toTag.getY(), toTag.getZ()});
	}

	@Override
	public long getStartTimestamp() {
		return fromTag.getTimestamp();
	}

	@Override
	public long getEndTimestamp() {
		return toTag.getTimestamp();
	}

	@Override
	public String getInfo() {
		return "{" + "\"from_x\": " + getFrom().getX() + ",\"from_y\": " +getFrom().getY() + ",\"to_x\": " + getTo().getX() + ",\"to_y\": " + getTo().getY() + ", moving: "+isMoving()+"}";
	}

}
