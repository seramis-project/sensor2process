package at.ac.wu.seramis.sensor2process.cep.sink;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class XESSink<T extends IBusinessEvent> extends RichSinkFunction<T>
{
	private static final long serialVersionUID = 7021634087616502373L;
	private final String filename;

	private XESSinkWriter xesSinkInstance = null;

	public XESSink(String filename){
		this.filename = filename;
	}

	@Override
	public void open(Configuration parameters) throws Exception
	{
		super.open(parameters);
		
		this.xesSinkInstance = XESSinkWriter.getInstance();
		this.xesSinkInstance.setFilename(filename);
	}
	
	@Override
	public void close() throws Exception
	{
		super.close();
		
		this.xesSinkInstance.close();
	}
	
	@Override
	public void invoke(T event) throws Exception 
	{
		this.xesSinkInstance.addEvent(event);		
	}
}