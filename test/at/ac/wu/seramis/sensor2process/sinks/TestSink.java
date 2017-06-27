package at.ac.wu.seramis.sensor2process.sinks;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreas on 11/18/16.
 */
public class TestSink implements SinkFunction<IBusinessEvent> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static List<IBusinessEvent> testObjects = new ArrayList<>();

    @Override
    public void invoke(IBusinessEvent value) throws Exception {
        testObjects.add(value);
    }

	public List<IBusinessEvent> getEvents() {
		// TODO Auto-generated method stub
		return testObjects;
	}
    
    
}
