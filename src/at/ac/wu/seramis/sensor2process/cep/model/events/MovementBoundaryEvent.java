package at.ac.wu.seramis.sensor2process.cep.model.events;

/**
 * Created by andreas on 11/9/16.
 */
public abstract class MovementBoundaryEvent extends AbstractBusinessEvent {

    protected long timestamp;
    protected double x, y, z;
    public TemporalPosition at;

    public MovementBoundaryEvent(String id) {
        super(id);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public TemporalPosition getAt() {
        return at;
    }

    public void setAt(TemporalPosition at) {
        this.at = at;
    }

    @Override
    public final String toString() {
        return "{" + getType() + ": id=" + this.id + ", timestamp=" + this.timestamp + "}";
    }
    
    @Override
	public long getStartTimestamp() {
		// TODO Auto-generated method stub
		return this.timestamp;
	}
	@Override
	public long getEndTimestamp() {
		// TODO Auto-generated method stub
		return this.timestamp;
	}
	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return "{" + "\"x\": " + x + ",\"y\": " + y + "}";
	}
    
}
