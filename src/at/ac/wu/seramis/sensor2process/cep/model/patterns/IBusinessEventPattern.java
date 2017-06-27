package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import org.apache.flink.cep.PatternFlatSelectFunction;
import org.apache.flink.cep.pattern.Pattern;

import java.io.Serializable;
import java.util.Map;

public interface IBusinessEventPattern<TEventType, TBusinessEventType extends IBusinessEvent> extends Serializable
{

	TBusinessEventType create(Map<String, TEventType> pattern);

	Pattern<TEventType, ? extends TEventType> getEventPattern();

	PatternFlatSelectFunction<TEventType, TBusinessEventType> getPatternFlatSelectFunction();

}
