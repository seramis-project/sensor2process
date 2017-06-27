package at.ac.wu.seramis.sensor2process.cep.model.events;

public class ToryPositionEvent extends PositionEvent
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String itemClass;

	public ToryPositionEvent()
	{
		this.timestamp = 0L;
	}

	public String getItemClass()
	{
		return this.itemClass;
	}

	public void setItemClass(String itemClass)
	{
		this.itemClass = itemClass;
	}

	public boolean equals(ToryPositionEvent positionEvent)
	{
		return this.id.equals(positionEvent.getId());
	}
}
