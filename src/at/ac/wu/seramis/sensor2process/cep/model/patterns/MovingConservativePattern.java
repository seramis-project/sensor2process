package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingConservative;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingTrivial;
import org.apache.flink.cep.pattern.Pattern;

import java.util.Map;

public class MovingConservativePattern extends AbstractBusinessEventPattern<IBusinessEvent>

{
    private static final double THRESHOLD = 5;

    public MovingConservativePattern() {
    }

    @Override
    public MovingConservative create(Map<String, IBusinessEvent> pattern) {
        MovingTrivial firstTrivial = (MovingTrivial) pattern.get("firstMove");
        MovingTrivial secondTrivial = (MovingTrivial) pattern.get("secondMove");
        MovingTrivial movingTrivial = new MovingTrivial(firstTrivial.getFrom(), secondTrivial.getTo());

        if (movingTrivial.getDistance() > THRESHOLD) {
            return new MovingConservative(firstTrivial, secondTrivial, movingTrivial);
        }
        return null;
    }


    @Override
    public Pattern<IBusinessEvent, MovingTrivial> getEventPattern() {
        return Pattern.<IBusinessEvent>begin("firstMove").subtype(MovingTrivial.class).where(evt -> evt.isMoving()).next("secondMove").subtype(MovingTrivial.class).where(evt -> evt.isMoving());
    }

    @Override
    public String getPatternCode() {
        return "movingConservative()";
    }
}
