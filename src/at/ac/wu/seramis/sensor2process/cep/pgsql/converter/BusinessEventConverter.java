package at.ac.wu.seramis.sensor2process.cep.pgsql.converter;

import at.ac.wu.seramis.sensor2process.cep.model.events.*;
import at.ac.wu.seramis.sensor2process.cep.pgsql.model.BusinessEvent;

public class BusinessEventConverter
{
	public static BusinessEvent convert(IBusinessEvent ibusinessEvent)
	{

		BusinessEvent businessEvent = null;
		switch (ibusinessEvent.getType())
		{
			case PositionBusinessEvent.FITTING_ROOM_TYPE:
			case PositionBusinessEvent.POINT_OF_SALES_TYPE:
				PositionBusinessEvent pbe = (PositionBusinessEvent) ibusinessEvent;
				businessEvent = new BusinessEvent(pbe.getTag().getId(), pbe.getTag().getTimestamp(), ibusinessEvent.getType(), "{\n" + "\"x\": " + pbe.getTag().getX() + ",\n\"y\": " + pbe.getTag().getY() + "\n}");
				break;
			case StartsMoving.TYPE:
			case StopsMoving.TYPE:
				MovementBoundaryEvent moving = (MovementBoundaryEvent)ibusinessEvent;
				businessEvent = new BusinessEvent(moving.getId(), moving.getTimestamp(),moving.getType(), moving.getInfo());
				break;
			case MovingTrivial.TYPE:
				MovingTrivial mte = (MovingTrivial) ibusinessEvent;
				businessEvent = new BusinessEvent(mte.getId(), mte.getTo().getTimestamp(), ibusinessEvent.getType(), "{\n" + "\"from_x\": " + mte.getFrom().getX() + ",\n\"from_y\": " + mte.getFrom().getY() + ",\n\"to_x\": " + mte.getTo().getX() + ",\n\"to_y\": " + mte.getTo().getY() + "\n}");
				break;
			default:
				break;
		}
		return businessEvent;
	}

	public static IBusinessEvent convert(BusinessEvent businessEvent)
	{

		IBusinessEvent iBusinessEvent = null;
		switch (businessEvent.getType())
		{
			case PositionBusinessEvent.FITTING_ROOM_TYPE:
				iBusinessEvent = new FittingRoomBusinessEvent(businessEvent);
				break;
			case MovingTrivial.TYPE:
				iBusinessEvent = new MovingTrivial(businessEvent);
				break;
			default:
				break;
		}
		return iBusinessEvent;

	}

}
