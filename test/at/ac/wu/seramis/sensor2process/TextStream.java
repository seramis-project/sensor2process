package at.ac.wu.seramis.sensor2process;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import at.ac.wu.seramis.sensor2process.simulation.Buffer;
import at.ac.wu.seramis.sensor2process.simulation.DatabaseBuffer;
import at.ac.wu.seramis.sensor2process.simulation.RawRead;
import javafx.collections.ObservableList;

/**
 * Created by andreas on 11/22/16.
 */
class TextStream<T> implements SourceFunction<String>
{
	private static final long serialVersionUID = -8171568396068656378L;
	private long t1;
	private long t2;

	public TextStream(long t1, long t2)
	{
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public void run(SourceFunction.SourceContext<String> ctx) throws Exception
	{

		Buffer dbBuffer = new DatabaseBuffer(this.t1, this.t2);
		// Buffer dbBuffer = new TestBuffer();
		ObservableList<Object> buffer = dbBuffer.getBuffer();
		dbBuffer.start();

		long timediff = 0;

		boolean running = true;
		while (running)
		{
			if (buffer.size() > 0)
			{
				RawRead entry = (RawRead) buffer.remove(0);
				if (entry == null)
				{
					ctx.close();
					running = false;
				}
				else
				{
					String s = (entry).toString();
					long timestamp = Long.parseLong(s.substring(s.length() - 13, s.length()));
					ctx.collectWithTimestamp(s, timestamp);
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
