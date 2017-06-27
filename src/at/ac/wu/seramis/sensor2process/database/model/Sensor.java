package at.ac.wu.seramis.sensor2process.database.model;

public class Sensor 
{
	private String sensorID = ""; // id_device in configuration_rtls.csv
	private String zone =""; // zone in configuration_rtls.csv
	
	private int antenna = 0; // antenna in configuration_rtls.csv
	
	private double posX = 0.0; // pos_x in configuration_rtls.csv
	private double posY = 0.0; // pos_y in configuration_rtls.csv
	private double rangeX = 0.0; // range_x in configuration_rtls.csv
	private double rangeY = 0.0; // range_y in configuration_rtls.csv
	
	public Sensor(String sensorID, int antenna, String zone, double posX, double posY, double rangeX, double rangeY)
	{
		this.sensorID = sensorID;
		this.antenna = antenna;
		this.zone = zone;
		this.posX = posX;
		this.posY = posY;
		this.rangeX = rangeX;
		this.rangeY = rangeY;
	}
	
	public double getPosX()
	{
		return this.posX;
	}
	
	public double getPosY()
	{
		return this.posY;
	}

	@Override
	public String toString() {
		return "Sensor [sensorID=" + sensorID + ", zone=" + zone + ", antenna=" + antenna + ", posX=" + posX + ", posY="
				+ posY + ", rangeX=" + rangeX + ", rangeY=" + rangeY + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + antenna;
		long temp;
		temp = Double.doubleToLongBits(posX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(posY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rangeX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rangeY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((sensorID == null) ? 0 : sensorID.hashCode());
		result = prime * result + ((zone == null) ? 0 : zone.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensor other = (Sensor) obj;
		if (antenna != other.antenna)
			return false;
		if (Double.doubleToLongBits(posX) != Double.doubleToLongBits(other.posX))
			return false;
		if (Double.doubleToLongBits(posY) != Double.doubleToLongBits(other.posY))
			return false;
		if (Double.doubleToLongBits(rangeX) != Double.doubleToLongBits(other.rangeX))
			return false;
		if (Double.doubleToLongBits(rangeY) != Double.doubleToLongBits(other.rangeY))
			return false;
		if (sensorID == null) {
			if (other.sensorID != null)
				return false;
		} else if (!sensorID.equals(other.sensorID))
			return false;
		if (zone == null) {
			if (other.zone != null)
				return false;
		} else if (!zone.equals(other.zone))
			return false;
		return true;
	}
	
	
	
	
	
}
