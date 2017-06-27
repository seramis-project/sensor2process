package at.ac.wu.seramis.sensor2process.cep.sink;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;

public class CSVSink<T extends IBusinessEvent> extends RichSinkFunction<T>
{
	private static final long serialVersionUID = 5613717897901326077L;
	public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

	private CSVSinkWriter csvSinkInstance = null;

	@Override
	public synchronized void open(Configuration parameters) throws Exception
	{
		super.open(parameters);

		this.csvSinkInstance = CSVSinkWriter.getInstance("caseId;type;start_timestamp;end_timestamp;info", "csvSink.csv");
	}

	@Override
	public void close() throws Exception
	{
		super.close();

		this.csvSinkInstance.close();
	}

	@Override
	public void invoke(T businessEvent) throws Exception
	{
		this.csvSinkInstance.writeLine(businessEvent.getId(), businessEvent.getType(), Instant.ofEpochMilli(businessEvent.getStartTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DATE_TIME_FORMAT), Instant.ofEpochMilli(businessEvent.getEndTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DATE_TIME_FORMAT), businessEvent.getInfo());
	}
}