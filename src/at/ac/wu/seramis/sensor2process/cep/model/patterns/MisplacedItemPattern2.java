package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import java.util.Map;

import org.apache.flink.cep.pattern.Pattern;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MisplacedItem;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingTrivial;
import at.ac.wu.seramis.sensor2process.cep.model.events.PositionEvent;
import at.ac.wu.seramis.sensor2process.utils.NearestNeighbours;

public class MisplacedItemPattern2 extends AbstractBusinessEventPattern<IBusinessEvent>
{
	private static final long serialVersionUID = 1L;

	public MisplacedItemPattern2()
	{

	}

	@Override
	public MisplacedItem create(Map<String, IBusinessEvent> pattern)
	{
		MovingTrivial movingEvent = (MovingTrivial) pattern.get("movingEvent");

		return new MisplacedItem((PositionEvent) movingEvent.getFrom(), (PositionEvent) movingEvent.getTo(), movingEvent.getDistance(), movingEvent.getQuotaFrom(), movingEvent.getQuotaTo());
	}

	@Override
	public Pattern<IBusinessEvent, MovingTrivial> getEventPattern()
	{
		return Pattern.<IBusinessEvent> begin("movingEvent").subtype(MovingTrivial.class).where(evt ->
		{
			// if (evt.isMoving() && CSVCorrectBuffer.getClassesMap().containsKey(evt.getId().subSequence(0, 12))) // only consider EPCs with available class information for Erfurt experiment
			if(evt.isMoving()) // DiTe experiment has class for every EPC
			{
				double quotaBefore = -1, quotaAfter = -1;

				quotaBefore = NearestNeighbours.getNearestNeighbourQuota(evt.getId(), 10);
				//NearestNeighbours.updatePosition(evt.getId(), CSVCorrectBuffer.getClassesMap().get(evt.getId().subSequence(0, 12)).toString(), evt.getTo().getX(), evt.getTo().getY()); // Erfurt class information
				NearestNeighbours.updatePosition(evt.getId(), evt.getId().substring(21, 23), evt.getTo().getX(), evt.getTo().getY()); // DiTe class information is contained in the epc
				quotaAfter = NearestNeighbours.getNearestNeighbourQuota(evt.getId(), 10);

				evt.setQuotaFrom(quotaBefore);
				evt.setQuotaTo(quotaAfter);
				return true;

				// return (quotaBefore > 0 && quotaAfter + 0.2 < quotaBefore);
			}

			return false;
		});
	}

	@Override
	public String getPatternCode()
	{
		return "misplacedItem()";
	}

}
