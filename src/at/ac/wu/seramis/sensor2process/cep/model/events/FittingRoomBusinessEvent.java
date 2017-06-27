package at.ac.wu.seramis.sensor2process.cep.model.events;

import at.ac.wu.seramis.sensor2process.cep.pgsql.model.BusinessEvent;

public class FittingRoomBusinessEvent extends PositionBusinessEvent
{

	public FittingRoomBusinessEvent(BusinessEvent businessEvent) {
		super(businessEvent, FITTING_ROOM_TYPE);
	}

	public FittingRoomBusinessEvent(TemporalPosition tag) {
		super(tag, FITTING_ROOM_TYPE);
	}
}
