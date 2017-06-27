package at.ac.wu.seramis.sensor2process.simulation;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class RawRead
{
	private String epc = "", device = "";
	private int antenna = -1, rssi = 0;
	private LocalDateTime timestamp = LocalDateTime.MIN;

	public RawRead(String epc, String device, int antenna, int rssi, LocalDateTime timestamp)
	{
		this.epc = epc;
		this.device = device;
		this.antenna = antenna;
		this.rssi = rssi;
		this.timestamp = timestamp;
	}

	@Override
	public String toString()
	{
		return this.epc + ";" + this.device + ";" + this.antenna + ";" + this.rssi + ";" + this.timestamp.toInstant(ZoneOffset.ofHours(0)).toEpochMilli();
	}

	public LocalDateTime getTimestamp()
	{
		return this.timestamp;
	}

}
