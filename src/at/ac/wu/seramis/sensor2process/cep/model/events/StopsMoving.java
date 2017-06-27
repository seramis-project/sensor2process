package at.ac.wu.seramis.sensor2process.cep.model.events;

public class StopsMoving extends MovementBoundaryEvent
{

	public static final String TYPE = "stops_moving";

	public StopsMoving(MovingConservative move) {
		super(move.getId());
		this.at = move.getTrivial23().getTo();
		this.timestamp = move.getTrivial12().getFrom().getTimestamp();
		this.x = move.getTrivial12().getFrom().getX();
		this.y = move.getTrivial12().getFrom().getY();
		this.z = move.getTrivial12().getFrom().getZ();
	}

	public StopsMoving(TemporalPosition tag1, TemporalPosition tag2, TemporalPosition tag3, TemporalPosition tag4, TemporalPosition tag5)
	{
		super(tag3.getId());
		this.timestamp = tag3.getTimestamp();
		this.x = tag3.getX();
		this.y = tag3.getY();
		this.z = tag3.getZ();
	}

	public StopsMoving(TemporalPosition instantOfStopping) {
		super(instantOfStopping.getId());
		this.timestamp = instantOfStopping.getTimestamp();
		this.x = instantOfStopping.getX();
		this.y = instantOfStopping.getY();
		this.z = instantOfStopping.getZ();
	}

	@Override
	public String getType()
	{
		return TYPE;
	}
}
