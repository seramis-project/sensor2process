package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import java.util.Map;

import org.apache.flink.cep.pattern.Pattern;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MisplacedItem;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingTrivial;
import at.ac.wu.seramis.sensor2process.cep.model.events.PositionEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import at.ac.wu.seramis.sensor2process.cep.model.events.ToryPositionEvent;
import at.ac.wu.seramis.sensor2process.utils.NearestNeighbours;

public class MisplacedItemPattern extends AbstractBusinessEventPattern<TemporalPosition>

{

	private static final long serialVersionUID = 1L;

	public MisplacedItemPattern()
	{

	}

	@Override
	public MisplacedItem create(Map<String, TemporalPosition> pattern)
	{

		PositionEvent firstLocation = (PositionEvent) pattern.get("firstLocation");
		PositionEvent secondLocation = (PositionEvent) pattern.get("secondLocation");

		return new MisplacedItem(firstLocation, secondLocation);

	}

	@Override
	public Pattern<TemporalPosition, TemporalPosition> getEventPattern()
	{
		return Pattern.<TemporalPosition>begin("firstLocation").next("secondLocation").subtype(TemporalPosition.class).where(evt ->
		{
			double quotaBefore = 0, quotaAfter = 0;

			quotaBefore = NearestNeighbours.getNearestNeighbourQuota(evt.getId(), 10);

			NearestNeighbours.updatePosition(evt.getId(), ((ToryPositionEvent) evt).getItemClass(), evt.getX(), evt.getY());

			quotaAfter = NearestNeighbours.getNearestNeighbourQuota(evt.getId(), 10);

			return (quotaBefore > 0 && quotaAfter + 0.2 < quotaBefore);
		});
	}

	@Override
	public String getPatternCode()
	{
		return "misplacedItem()";
	}

}
