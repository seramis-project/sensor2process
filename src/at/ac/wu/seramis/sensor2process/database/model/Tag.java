package at.ac.wu.seramis.sensor2process.database.model;

import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalSenseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Tag implements TemporalPosition, TemporalSenseEvent
{
	private static final long serialVersionUID = -3803435021815974355L;

	private static final int CIRCLE_SIZE = 3;
	private double x = 0, y = 0, z = 0;
	private double rssi = 0;
	private long time_millis;

	private String actor = "";

	public Tag(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Tag(double x, double y, double rssi)
	{
		this.x = x;
		this.y = y;
		this.rssi = rssi;
	}

	public Tag()
	{
		super();
	}

	@Override
	public double getCertainty() {
		return getRssi();
	}

	@Override
	public void setCertainty(double certainty) {
		setRssi(certainty);
	}

	public double getRssi()
	{
		return this.rssi;
	}

	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public double getSignalStrength() {
		return getRssi();
	}

	@Override
	public long getTimestamp() {
		return this.getTime_millis();
	}

	@Override
	public String getId() {
		return this.getActor();
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public void setRssi(double rssi)
	{
		this.rssi = rssi;
	}

	public String getActor()
	{
		return this.actor;
	}

	public void setActor(String actor)
	{
		this.actor = actor;
	}

	@Override
	public void setId(String id) {
		setActor(id);
	}

	@Override
	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public void setSignalStrength(double signalStrength) {
		setRssi(signalStrength);
	}

	@Override
	public void setTimestamp(long timestamp) {
		this.setTime_millis(timestamp);
	}

	public Shape getShape(Grid2 g, double index, double size)
	{
		Circle tag = new Circle(g.computeX(this.x), g.computeY(this.y), CIRCLE_SIZE);
		// tag.setFill(Color.color(1 - index/size, index/size, index/size));;
		// tag.setFill(Color.color(1 - index/size, index/size *0.9,
		// index/size*0.8));;
		if (index == -1)
		{

			tag.setFill(Color.RED);
			;
		}
		else
		{
			tag.setFill(Color.hsb(index / size * 360, 1, 0.8).darker());
			;

		}

		return tag;
	}

	@Override
	public String toString()
	{
		return "Tag [x=" + this.x + ", y=" + this.y + ", rssi=" + this.rssi +", epc: "+this.actor +"]";
	}

	public Node getShape(Grid2 g)
	{
		// TODO Auto-generated method stub
		Circle tag = new Circle(g.computeX(this.x), g.computeY(this.y), 4);
		tag.setFill(Color.LIME);

		return tag;
	}

	public long getTime_millis()
	{
		return this.time_millis;
	}

	public void setTime_millis(long time_millis)
	{
		this.time_millis = time_millis;
	}

}
