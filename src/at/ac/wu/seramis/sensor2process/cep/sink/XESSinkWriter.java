package at.ac.wu.seramis.sensor2process.cep.sink;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.out.XesXmlSerializer;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

public class XESSinkWriter
{	
	private static final String _OUTPUT_PATH = "sink.xes"; 
	
	private static XESSinkWriter _instance;
	private static int _instanceCount = 0;
	
	private HashMap<String, XTrace> xesTraces = new HashMap<>();
	
	private XFactory xesFactory;
	private XLog xesLog;

	private String filename;

	public XESSinkWriter()
	{
		this.xesFactory = new XFactoryNaiveImpl();
		this.xesLog = this.xesFactory.createLog();
		this.filename = _OUTPUT_PATH;
	}
	
	public synchronized void addEvent(IBusinessEvent event)
	{
		// get existing XTrace for this EPC (or create new one, if first occurrence)
		if (!this.xesTraces.containsKey(event.getId())){
			XTrace newTrace = this.xesFactory.createTrace();
			XConceptExtension.instance().assignName(newTrace, event.getId());
			this.xesTraces.put(event.getId(), newTrace);
		}
		XTrace xesTrace = this.xesTraces.get(event.getId());
				
		// create XEvent out of sinked IBusinessEvent
		if (event.getStartTimestamp() != 0) {
			addEvent(event, xesTrace, event.getStartTimestamp(), XLifecycleExtension.StandardModel.START);
		}
		if (event.getEndTimestamp() != 0 && event.getEndTimestamp() != event.getStartTimestamp()) {
			addEvent(event, xesTrace, event.getEndTimestamp(), XLifecycleExtension.StandardModel.COMPLETE);
		}
	}

	private void addEvent(IBusinessEvent event, XTrace xesTrace, long timestamp, XLifecycleExtension.StandardModel lifecycle) {
		XEvent xesEvent = this.xesFactory.createEvent();
		XConceptExtension.instance().assignName(xesEvent, event.getType());
		XTimeExtension.instance().assignTimestamp(xesEvent, timestamp);
		xesEvent.getAttributes().put("info", new XAttributeLiteralImpl("info", event.getInfo()));
		XLifecycleExtension.instance().assignStandardTransition(xesEvent, lifecycle);
		xesTrace.add(xesEvent);
	}

	public void close()
	{
		_instanceCount--;
		
		if(_instanceCount <= 0)
		{			
			for(Entry<String, XTrace> xesTrace : this.xesTraces.entrySet())
			{
				XConceptExtension.instance().assignName(xesTrace.getValue(), xesTrace.getKey());
				this.xesLog.add(xesTrace.getValue());
			}
			
			try
			{
				new XesXmlSerializer().serialize(this.xesLog, new FileOutputStream(filename));
			}
			catch(Exception e)
			{
				System.err.println("Could not write to XES file \"" + filename + "\".");
				e.printStackTrace();
			}
		}
	}
	
	public static XESSinkWriter getInstance()
	{
		if(_instance == null)
		{
			synchronized (XESSinkWriter.class) 
			{
				if(_instance == null)
				{
					_instance = new XESSinkWriter();
				}
			}
		}
		
		_instanceCount++;
		return _instance;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
