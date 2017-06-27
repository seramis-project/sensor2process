package at.ac.wu.seramis.sensor2process.cep.model.events;

public class MisplacedItem extends AbstractBusinessEvent
{

	private PositionEvent firstPosition;
	private PositionEvent secondPosition;

	private double distance = -1, firstQuota = -1, secondQuota = -1;

	private boolean misplaced = false;

	public boolean isMisplaced()
	{
		return this.misplaced;
	}

	public void setMisplaced(boolean misplaced)
	{
		this.misplaced = misplaced;
	}

	@Override
	public String getType()
	{
		return "MisplacedItem";
	}

	public MisplacedItem(PositionEvent firstPosition, PositionEvent secondPosition)
	{
		super(firstPosition.getId());
		this.firstPosition = firstPosition;
		this.secondPosition = secondPosition;

	}

	public MisplacedItem(PositionEvent firstPosition, PositionEvent secondPosition, double distance, double firstQuota, double secondQuota)
	{
		this(firstPosition, secondPosition);

		this.distance = distance;
		this.firstQuota = firstQuota;
		this.secondQuota = secondQuota;
	}

	@Override
	public String toString()
	{
		// return "MisplacedItem [firstPosition=" +
		// this.firstPosition.toString() + ", secondPosition=" +
		// this.secondPosition.toString() + "]";
		return "MisplacedItem " + this.id + " [first={x:" + this.firstPosition.getX() + "\ty:" + this.firstPosition.getY() + "\tquota:" + this.firstQuota + "}, second={x:" + this.secondPosition.getX() + "\ty:" + this.secondPosition.getY() + "\tquota:" + this.secondQuota + "}, distance=" + this.distance + "]";
	}

	@Override
	public long getStartTimestamp()
	{
		return this.firstPosition.getTimestamp();
	}

	@Override
	public long getEndTimestamp()
	{
		return this.secondPosition.getTimestamp();
	}

	public PositionEvent getFirstPosition()
	{
		return this.firstPosition;
	}

	public void setFirstPosition(PositionEvent firstPosition)
	{
		this.firstPosition = firstPosition;
	}

	public PositionEvent getSecondPosition()
	{
		return this.secondPosition;
	}

	public void setSecondPosition(PositionEvent secondPosition)
	{
		this.secondPosition = secondPosition;
	}

	public double getDistance()
	{
		return this.distance;
	}

	public double getFirstQuota()
	{
		return this.firstQuota;
	}

	public double getSecondQuota()
	{
		return this.secondQuota;
	}

	@Override
	public String getInfo()
	{
		return this.toString();
	}

}
