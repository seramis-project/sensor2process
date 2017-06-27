package at.ac.wu.seramis.sensor2process.cep.pgsql.mapping;

import at.ac.wu.seramis.sensor2process.cep.pgsql.model.BusinessEvent;
import de.bytefish.pgbulkinsert.PgBulkInsert;

public class BusinessEventBulkInsert extends PgBulkInsert<BusinessEvent>
{

	public BusinessEventBulkInsert(String schemaName, String tableName)
	{
		super(schemaName, tableName);

		this.mapString("epc", BusinessEvent::getEpc);

		this.mapLong("timestamp", BusinessEvent::getTimestamp);

		this.mapString("type", BusinessEvent::getType);

		this.mapString("info", BusinessEvent::getInfo);

	}

}
