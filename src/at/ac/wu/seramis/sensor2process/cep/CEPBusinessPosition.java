package at.ac.wu.seramis.sensor2process.cep;

import at.ac.wu.seramis.sensor2process.cep.model.events.*;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.IBusinessEventPattern;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.util.Collector;

import java.util.Map;

public class CEPBusinessPosition {
    public static <TBusinessEventType extends IBusinessEvent> DataStream<IBusinessEvent> toBusinessEventsStream(DataStream<TemporalPosition> tagStream, IBusinessEventPattern<TemporalPosition, TBusinessEventType> businessEventPattern)
    {
        PatternStream<TemporalPosition> tempPatternStream = CEP.pattern(tagStream.keyBy(new KeySelector<TemporalPosition, String>() {
            @Override
            public String getKey(TemporalPosition value) throws Exception {
                return value.getId();
            }
        }), businessEventPattern.getEventPattern());

        DataStream<IBusinessEvent> businessEvents = tempPatternStream.select((PatternSelectFunction<TemporalPosition, IBusinessEvent>) map -> businessEventPattern.create(map));
        return businessEvents;
    }

    public static <TBusinessEventType extends IBusinessEvent> DataStream<IBusinessEvent> toFlatPositionBusinessEventsStream(DataStream<TemporalPosition> tagStream, IBusinessEventPattern<TemporalPosition, IBusinessEvent> businessEventPattern)
    {
        PatternStream<TemporalPosition> tempPatternStream = CEP.pattern(tagStream.keyBy(t -> t.getId()), businessEventPattern.getEventPattern());

        DataStream<IBusinessEvent> businessEvents = tempPatternStream.flatSelect(businessEventPattern.getPatternFlatSelectFunction());
        return businessEvents;
    }

    public static <In extends IBusinessEvent, Out extends IBusinessEvent> DataStream<Out> toFlatBusinessEventsStream(DataStream<In> tagStream, IBusinessEventPattern<In, Out> businessEventPattern)
    {
        PatternStream<In> tempPatternStream = CEP.pattern(tagStream.keyBy(new KeySelector<In, String>() {
            @Override
            public String getKey(In t) throws Exception {
                return t.getId();
            }
        }), businessEventPattern.getEventPattern());

        DataStream<Out> businessEvents = tempPatternStream.flatSelect(businessEventPattern.getPatternFlatSelectFunction());
        return businessEvents;
    }

    public static <TBusinessEventType extends IBusinessEvent> DataStream<IBusinessEvent> toMovementStream(DataStream<TemporalPosition> tagStream, IBusinessEventPattern<TemporalPosition, TBusinessEventType> businessEventPattern)
    {
        PatternStream<TemporalPosition> tempPatternStream = CEP.pattern(tagStream.keyBy(tag -> tag.getId()), businessEventPattern.getEventPattern());

        DataStream<IBusinessEvent> movingEvents = tempPatternStream.flatSelect((Map<String, TemporalPosition> pattern, Collector<IBusinessEvent> out) ->
        {
            TemporalPosition fromTag = pattern.get("First Tag");
            TemporalPosition toTag = pattern.get("Second Tag");
            MovingTrivial movingTrivial = new MovingTrivial(fromTag, toTag);
            if (movingTrivial.getDistance() > 4)
            {
                out.collect(movingTrivial);
            }
        });

        return movingEvents;
    }

    public static <TBusinessEventType extends IBusinessEvent> DataStream<IBusinessEvent> toStartsMoving(DataStream<TemporalPosition> tagStream, IBusinessEventPattern<TemporalPosition, TBusinessEventType> businessEventPattern)
    {
        PatternStream<TemporalPosition> tempPatternStream = CEP.pattern(tagStream.keyBy(tag -> tag.getId()), businessEventPattern.getEventPattern());

        DataStream<IBusinessEvent> movingEvents = tempPatternStream.flatSelect((Map<String, TemporalPosition> pattern, Collector<IBusinessEvent> out) ->
        {
            TemporalPosition t1 = pattern.get("First Tag");
            TemporalPosition t2 = pattern.get("Second Tag");
            TemporalPosition t3 = pattern.get("Third Tag");
            TemporalPosition t4 = pattern.get("Fourth Tag");
            TemporalPosition t5 = pattern.get("Fifth Tag");

            MovingTrivial trivial12 = new MovingTrivial(t1, t2);
            MovingTrivial trivial23 = new MovingTrivial(t2, t3);

            if (trivial12.getDistance() < 4 && trivial23.getDistance() < 4)
            {
                MovingTrivial trivial34 = new MovingTrivial(t3, t4);
                MovingTrivial trivial45 = new MovingTrivial(t4, t5);
                MovingTrivial trivial35 = new MovingTrivial(t3, t5);
                if (trivial34.getDistance() > 4 && trivial45.getDistance() > 4 && trivial35.getDistance() > 4)
                {
                    out.collect(new StartsMoving(t1, t2, t3, t4, t5));
                }
            }

        });

        return movingEvents;
    }

    public static <TBusinessEventType extends IBusinessEvent> DataStream<IBusinessEvent> toStopsMoving(DataStream<TemporalPosition> tagStream, IBusinessEventPattern<TemporalPosition, TBusinessEventType> businessEventPattern)
    {
        PatternStream<TemporalPosition> tempPatternStream = CEP.pattern(tagStream.keyBy(tag -> tag.getId()), businessEventPattern.getEventPattern());

        DataStream<IBusinessEvent> movingEvents = tempPatternStream.flatSelect((Map<String, TemporalPosition> pattern, Collector<IBusinessEvent> out) ->
        {
            TemporalPosition t1 = pattern.get("First Tag");
            TemporalPosition t2 = pattern.get("Second Tag");
            TemporalPosition t3 = pattern.get("Third Tag");
            TemporalPosition t4 = pattern.get("Fourth Tag");
            TemporalPosition t5 = pattern.get("Fifth Tag");

            MovingTrivial trivial12 = new MovingTrivial(t1, t2);
            MovingTrivial trivial23 = new MovingTrivial(t2, t3);
            MovingTrivial trivial34 = new MovingTrivial(t3, t4);
            MovingTrivial trivial45 = new MovingTrivial(t4, t5);
            MovingTrivial trivial13 = new MovingTrivial(t1, t3);

            if (trivial34.getDistance() < 4 && trivial45.getDistance() < 4)
            {

                if (trivial12.getDistance() > 4 && trivial23.getDistance() > 4 && trivial13.getDistance() > 4)
                {
                    out.collect(new StopsMoving(t1, t2, t3, t4, t5));
                }
            }

        });

        return movingEvents;
    }

    public static <TBusinessEventType extends IBusinessEvent> DataStream<IBusinessEvent> toConservativeStream(DataStream<IBusinessEvent> movingTrivialStream, IBusinessEventPattern<MovingTrivial, TBusinessEventType> businessEventPattern)
    {
        ;
        PatternStream<MovingTrivial> tempPatternStream = CEP.pattern(movingTrivialStream.map(new MapFunction<IBusinessEvent, MovingTrivial>() {
                                                                 @Override
                                                                 public MovingTrivial map(IBusinessEvent evt) throws Exception {
                                                                     return (MovingTrivial) evt;
                                                                 }
                                                             }).keyBy(movingTrivial -> movingTrivial.getFrom().getId()), businessEventPattern.getEventPattern());

        DataStream<IBusinessEvent> movingEvents = tempPatternStream.flatSelect((Map<String, MovingTrivial> pattern, Collector<IBusinessEvent> out) ->
        {
            MovingTrivial firstTrivial = pattern.get("First Trivial");
            MovingTrivial secondTrivial = pattern.get("Second Trivial");
            MovingTrivial movingTrivial = new MovingTrivial(firstTrivial.getFrom(), secondTrivial.getTo());
            if (movingTrivial.getDistance() > 4)
            {
                out.collect(new MovingConservative(firstTrivial, secondTrivial, movingTrivial));
            }
        });

        return movingEvents;
    }

}
