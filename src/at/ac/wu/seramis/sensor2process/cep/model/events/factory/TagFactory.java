package at.ac.wu.seramis.sensor2process.cep.model.events.factory;

import at.ac.wu.seramis.sensor2process.database.model.Tag;

/**
 * Created by andreas on 11/18/16.
 */
public class TagFactory extends TemporalPositionFactory<Tag> {
    @Override
    public Tag create() {
        return new Tag();
    }
}
