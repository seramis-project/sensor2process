package at.ac.wu.seramis.sensor2process.cep.model.events;

public class MovingConservative extends AbstractBusinessEvent
{

	private boolean moving;

	private MovingTrivial trivial12;
	private MovingTrivial trivial23;
	private MovingTrivial trivial13;

	// public MovingConservative(BusinessEvent businessEvent)
	// {
	// JSONObject obj = new JSONObject(businessEvent.getInfo());
	//
	// Tag toTag = new Tag(obj.getDouble("to_x"), obj.getDouble("to_y"));
	// Tag fromTag = new Tag(obj.getDouble("from_x"), obj.getDouble("from_y"));
	// toTag.setActor(businessEvent.getEpc());
	// fromTag.setActor(businessEvent.getEpc());
	// fromTag.setTime_millis(businessEvent.getTimestamp());
	// toTag.setTime_millis(businessEvent.getTimestamp());
	// this.toTag = toTag;
	// this.fromTag = fromTag;
	//
	// }

	public MovingTrivial getTrivial13()
	{
		return this.trivial13;
	}

	public void setTrivial13(MovingTrivial trivial13)
	{
		this.trivial13 = trivial13;
	}

	@Override
	public String getType()
	{
		// TODO Auto-generated method stub
		return "moving_conservative";
	}

	public MovingTrivial getTrivial12()
	{
		return this.trivial12;
	}

	public void setTrivial12(MovingTrivial trivial12)
	{
		this.trivial12 = trivial12;
	}

	public MovingTrivial getTrivial23()
	{
		return this.trivial23;
	}

	public void setTrivial23(MovingTrivial trivial23)
	{
		this.trivial23 = trivial23;
	}

	public MovingConservative(MovingTrivial trivial12, MovingTrivial trivial23, MovingTrivial trivial13)
	{
		this(trivial12, trivial23, trivial13, true);
	}


	public MovingConservative(MovingTrivial trivial12, MovingTrivial trivial23, MovingTrivial trivial13, boolean moving)
	{
		super(trivial12.getId());
		this.trivial12 = trivial12;
		this.trivial23 = trivial23;
		this.trivial13 = trivial13;
		this.moving = moving;
	}

	@Override
	public String toString()
	{
		return "MovingConservative [moving="+this.moving+", trivial12=" + this.trivial12.toString() + ", trivial23=" + this.trivial23.toString() + ", trivial13=" + this.trivial13.toString() + "]";
	}

	public boolean isMoving()
	{
		return this.moving;
	}

	public void setMoving(boolean moving)
	{
		this.moving = moving;
	}

	@Override
	public long getStartTimestamp() {
		return this.trivial12.getStartTimestamp();
	}

	@Override
	public long getEndTimestamp() {
		return this.trivial23.getEndTimestamp();
	}

	@Override
	public String getInfo() {
		return "{" + "\"from_x\": " + getTrivial12().getFrom().getX() + ",\"from_y\": " +getTrivial12().getFrom().getY() +
				",\"to_x\": " + trivial23.getTo().getX() + ",\"to_y\": " + trivial23.getTo().getY() + "}";
	}

}
