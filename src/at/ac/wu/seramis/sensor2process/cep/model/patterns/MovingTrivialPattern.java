package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import java.util.Map;

import at.ac.wu.seramis.sensor2process.cep.model.events.MovingTrivial;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.flink.cep.pattern.Pattern;

public class MovingTrivialPattern extends AbstractBusinessEventPattern<TemporalPosition>
{
	private static final String FIRST = "first";
	private static final String SECOND = "second";
;
    private static final long serialVersionUID = -1445832209022149804L;

    private static DistanceMeasure distance = new EuclideanDistance();
    
	private static double _threshold = 6.5;

	public MovingTrivialPattern()
	{

	}

	public static void setThreshold(double threshold)
	{
		MovingTrivialPattern._threshold = threshold;
	}
	
	@Override
	public MovingTrivial create(Map<String, TemporalPosition> pattern)
	{
		TemporalPosition fromTag = pattern.get(FIRST);
		TemporalPosition toTag = pattern.get(SECOND);

		if (MovingTrivialPattern.getMoved(fromTag, toTag))
		{
			return new MovingTrivial(fromTag, toTag, true);
		}
		return new MovingTrivial(fromTag, toTag, false);
	}

	public static boolean getMoved(TemporalPosition from, TemporalPosition to){
		return distance.compute(new double[]{from.getX(), from.getY(), from.getZ()}, new double[]{to.getX(), to.getY(), to.getZ()}) > _threshold;
	}

	@Override
	public Pattern<TemporalPosition, TemporalPosition> getEventPattern() {
		return Pattern.<TemporalPosition>begin(FIRST).next(SECOND);
	}

	@Override
	public String getPatternCode() {
		return "movingTrivial()";
	}
}
