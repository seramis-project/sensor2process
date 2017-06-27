package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import java.util.Map;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingConservative;
import at.ac.wu.seramis.sensor2process.cep.model.events.MovingTrivial;
import at.ac.wu.seramis.sensor2process.cep.model.events.StopsMoving;
import org.apache.flink.cep.pattern.Pattern;

public class StopsMovingPattern extends AbstractBusinessEventPattern<IBusinessEvent>

{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public StopsMovingPattern() {

    }

    @Override
    public IBusinessEvent create(Map<String, IBusinessEvent> pattern) {
        MovingConservative move = (MovingConservative) pattern.get("move");

        return new StopsMoving(move);
    }

    @Override
    public Pattern<IBusinessEvent, MovingTrivial> getEventPattern() {
        return Pattern.<IBusinessEvent>begin("move").subtype(MovingConservative.class)
                .next("nomove").subtype(MovingTrivial.class).where(move -> !move.isMoving())
                .next("nomove2").subtype(MovingTrivial.class).where(move -> !move.isMoving());
    }

    @Override
    public String getPatternCode() {
        return "stoppedMoving()";
    }
}
