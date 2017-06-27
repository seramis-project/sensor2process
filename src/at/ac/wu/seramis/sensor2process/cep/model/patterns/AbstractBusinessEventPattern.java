package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import org.apache.flink.cep.PatternFlatSelectFunction;
import org.apache.flink.util.Collector;

import java.util.Map;

/**
 * Created by andreas on 11/9/16.
 */
public abstract class AbstractBusinessEventPattern<TSource> implements IBusinessEventPattern<TSource, IBusinessEvent> {

    private static final long serialVersionUID = 6943305728163516107L;

    public abstract String getPatternCode();

    public final PatternFlatSelectFunction<TSource, IBusinessEvent> getPatternFlatSelectFunction() {
        return (PatternFlatSelectFunction<TSource, IBusinessEvent>) (pattern, out) -> {
            IBusinessEvent type = create(pattern);
            if (type != null) {
                out.collect(type);
            }
        };
    }

}
