package at.ac.wu.seramis.sensor2process.cep.stream.sinks.pgsql;

import java.net.URI;

import at.ac.wu.seramis.sensor2process.cep.pgsql.mapping.BusinessEventBulkInsert;
import at.ac.wu.seramis.sensor2process.cep.pgsql.model.BusinessEvent;
import de.bytefish.pgbulkinsert.IPgBulkInsert;

public class BusinessEventPostgresSink extends BasePostgresSink<BusinessEvent>
{
	private static final long serialVersionUID = 5142367102612127422L;

	public BusinessEventPostgresSink(URI databaseUri, int bulkSize)
	{
		super(databaseUri, bulkSize);
	}

	@Override
	protected IPgBulkInsert<BusinessEvent> getBulkInsert()
	{
		return new BusinessEventBulkInsert("deliverable", "business_events_misplaced");

	}

}
