package at.ac.wu.seramis.sensor2process.cep.model.events;

public class StartsMoving extends MovementBoundaryEvent
{
	public static final String TYPE = "starts_moving";

	public StartsMoving(MovingConservative move) {
		super(move.getId());
		this.at = move.getTrivial12().getFrom();
		this.timestamp = move.getTrivial12().getFrom().getTimestamp();
		this.x = move.getTrivial12().getFrom().getX();
		this.y = move.getTrivial12().getFrom().getY();
		this.z = move.getTrivial12().getFrom().getZ();
	}
	public StartsMoving(TemporalPosition tag1, TemporalPosition tag2, TemporalPosition tag3, TemporalPosition tag4, TemporalPosition tag5)
	{
		super(tag1.getId());
        this.timestamp = tag3.getTimestamp();
        this.x = tag3.getX();
        this.y = tag3.getY();
        this.z = tag3.getZ();
	}

	public StartsMoving(TemporalPosition before) {
		super(before.getId());
		this.timestamp = before.getTimestamp();
		this.x = before.getX();
		this.y = before.getY();
		this.z = before.getZ();
	}

	@Override
	public String getType()
	{
		return TYPE;
	}

}
