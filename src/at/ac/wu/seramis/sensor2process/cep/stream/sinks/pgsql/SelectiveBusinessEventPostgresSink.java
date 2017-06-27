package at.ac.wu.seramis.sensor2process.cep.stream.sinks.pgsql;

import at.ac.wu.seramis.sensor2process.cep.pgsql.mapping.BusinessEventBulkInsert;
import at.ac.wu.seramis.sensor2process.cep.pgsql.model.BusinessEvent;
import de.bytefish.pgbulkinsert.IPgBulkInsert;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by andreas on 11/19/16.
 */
public class SelectiveBusinessEventPostgresSink extends BusinessEventPostgresSink {
    private final Set<String> eventsToStore;

    public SelectiveBusinessEventPostgresSink(URI databaseUri, int bulkSize, Collection<String> eventsToStore) {
        super(databaseUri, bulkSize);
        this.eventsToStore = new HashSet<>(eventsToStore);
    }

    @Override
    public void invoke(BusinessEvent businessEvent) throws Exception {
        if (eventsToStore.contains(businessEvent.getType())) {
            super.invoke(businessEvent);
        }
    }
    @Override
    protected IPgBulkInsert<BusinessEvent> getBulkInsert()
    {
        return new BusinessEventBulkInsert("deliverable", "business_events_all");
    }
}
