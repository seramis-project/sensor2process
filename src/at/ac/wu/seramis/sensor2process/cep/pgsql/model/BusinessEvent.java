package at.ac.wu.seramis.sensor2process.cep.pgsql.model;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;

public class BusinessEvent implements IBusinessEvent
{

	private String epc;
	private long timestamp;
	private String type;
	private String info;

	public BusinessEvent(String epc, long l, String type, String info)
	{
		super();
		this.epc = epc;
		this.timestamp = l;
		this.type = type;
		this.info = info;
	}

	public String getEpc()
	{
		return this.epc;
	}

	public void setEpc(String epc)
	{
		this.epc = epc;
	}

	public long getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getType()
	{
		return this.type;
	}

	@Override
	public String getId() {
		return getEpc();
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getInfo()
	{
		return this.info;
	}

	public void setInfo(String info)
	{
		this.info = info;
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

}
