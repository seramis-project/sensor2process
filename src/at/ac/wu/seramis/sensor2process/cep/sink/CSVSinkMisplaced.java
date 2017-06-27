package at.ac.wu.seramis.sensor2process.cep.sink;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MisplacedItem;

public class CSVSinkMisplaced<T extends IBusinessEvent> extends RichSinkFunction<T>
{
	private static final long serialVersionUID = 5613717897901326077L;
	public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

	private CSVSinkWriter csvSinkInstance = null;
	private String path = null;

	public CSVSinkMisplaced(String path)
	{
		super();
		this.path = path;
	}

	@Override
	public synchronized void open(Configuration parameters) throws Exception
	{
		super.open(parameters);

		this.csvSinkInstance = CSVSinkWriter.getInstance("caseId;start_timestamp;end_timestamp;firstQuota;secondQuota;distance;misplaced", this.path);
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
		MisplacedItem item = (MisplacedItem) businessEvent;

		this.csvSinkInstance.writeLine(item.getId(), Instant.ofEpochMilli(item.getStartTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DATE_TIME_FORMAT), Instant.ofEpochMilli(item.getEndTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DATE_TIME_FORMAT), "" + item.getFirstQuota(), "" + item.getSecondQuota(), "" + item.getDistance(), "" + item.isMisplaced());
	}
}