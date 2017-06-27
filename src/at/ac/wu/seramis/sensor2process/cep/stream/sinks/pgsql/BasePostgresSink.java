package at.ac.wu.seramis.sensor2process.cep.stream.sinks.pgsql;

import java.net.URI;

import at.ac.wu.seramis.sensor2process.cep.pgsql.connection.PooledConnectionFactory;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import de.bytefish.pgbulkinsert.IPgBulkInsert;
import de.bytefish.pgbulkinsert.pgsql.processor.BulkProcessor;
import de.bytefish.pgbulkinsert.pgsql.processor.handler.BulkWriteHandler;

public abstract class BasePostgresSink<TEntity> extends RichSinkFunction<TEntity>
{

	private final URI databaseUri;
	private final int bulkSize;

	private BulkProcessor<TEntity> bulkProcessor;

	public BasePostgresSink(URI databaseUri, int bulkSize)
	{
		this.databaseUri = databaseUri;
		this.bulkSize = bulkSize;
	}

	@Override
	public void invoke(TEntity entity) throws Exception
	{
		this.bulkProcessor.add(entity);
	}

	@Override
	public void open(Configuration parameters) throws Exception
	{
		this.bulkProcessor = new BulkProcessor<>(new BulkWriteHandler<>(this.getBulkInsert(), new PooledConnectionFactory(this.databaseUri)), this.bulkSize);
	}

	@Override
	public void close() throws Exception
	{
		if (this.bulkProcessor != null) {
			this.bulkProcessor.close();
		}
	}

	protected abstract IPgBulkInsert<TEntity> getBulkInsert();
}