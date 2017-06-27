package at.ac.wu.seramis.sensor2process.cep.model.events;

public interface IBusinessEvent
{
	String getType();

	String getId();

	long getStartTimestamp();

	long getEndTimestamp();

	String getInfo();
}
