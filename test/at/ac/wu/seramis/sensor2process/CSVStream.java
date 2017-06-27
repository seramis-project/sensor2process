package at.ac.wu.seramis.sensor2process;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import at.ac.wu.seramis.sensor2process.cep.model.events.AbstractPositionEvent;
import at.ac.wu.seramis.sensor2process.simulation.Buffer;
import at.ac.wu.seramis.sensor2process.simulation.CSVBuffer;
import javafx.collections.ObservableList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by andreas on 11/22/16.
 */
class CSVStream<T> implements SourceFunction<AbstractPositionEvent>
{
	private static final long serialVersionUID = -8171568396068656378L;
	private String csv;
	private String type;

	public CSVStream(String csv)
	{
		super();
		this.csv = csv;
		this.type = "standard";
	}

	public CSVStream<T> toCorrectStream()
	{
		this.type = "correct";
		return this;

	}

	@Override
	public void run(SourceFunction.SourceContext<AbstractPositionEvent> ctx) throws Exception
	{

		Buffer dbBuffer;
		dbBuffer = new CSVBuffer(this.csv);
		// Buffer dbBuffer = new TestBuffer();
		ObservableList<Object> buffer = dbBuffer.getBuffer();

		ExecutorService service = Executors.newFixedThreadPool(1);
		service.submit(dbBuffer);
		service.shutdown();
		service.awaitTermination(1, TimeUnit.DAYS);

		long timediff = 0;

		boolean running = true;
		while (running)
		{
			if (buffer.size() > 0)
			{
				AbstractPositionEvent entry = (AbstractPositionEvent) buffer.remove(0);
				if (entry == null)
				{
					// ctx.collect(null);
					ctx.close();
					running = false;
				}
				else
				{

					ctx.collect(entry);
				}
			}
			else
			{
				Thread.sleep(100);
			}
		}
	}

	@Override
	public void cancel()
	{
		// TODO Auto-generated method stub
	}

}
