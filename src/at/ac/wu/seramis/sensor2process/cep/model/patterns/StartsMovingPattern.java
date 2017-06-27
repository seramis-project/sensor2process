package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingConservative;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingTrivial;
import at.ac.wu.seramis.sensor2process.cep.model.events.StartsMoving;
import org.apache.flink.cep.pattern.Pattern;

import java.util.Map;



public class StartsMovingPattern extends AbstractBusinessEventPattern<IBusinessEvent>

{
	private static final double THRESHOLD = 5;

	@Override
	public StartsMoving create(Map<String, IBusinessEvent> pattern)
	{
		MovingConservative move = (MovingConservative)pattern.get("move");

		return new StartsMoving(move);
	}

	@Override
	public Pattern<IBusinessEvent, ? extends IBusinessEvent> getEventPattern()
	{
		return Pattern.<IBusinessEvent> begin("nomove").subtype(MovingTrivial.class).where(move -> !move.isMoving())
				.next("nomove2").subtype(MovingTrivial.class).where(move -> !move.isMoving())
				.next("move").subtype(MovingConservative.class);
	}

	@Override
	public String getPatternCode() {
		return "startsMoving()";
	}
}
