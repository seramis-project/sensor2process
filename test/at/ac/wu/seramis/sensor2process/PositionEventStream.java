package at.ac.wu.seramis.sensor2process;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;

/**
 * Created by andreas on 11/18/16.
 */
public class PositionEventStream implements SourceFunction<TemporalPosition> {
    private TemporalPosition[] events;
    public PositionEventStream(TemporalPosition...events) {
        this.events = events;
    }

    @Override
    public void run(SourceContext<TemporalPosition> ctx) throws Exception {
        for (TemporalPosition e : events){
            ctx.collect(e);
        }
        ctx.close();
    }

    @Override
    public void cancel() {

    }
}
