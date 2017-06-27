package at.ac.wu.seramis.sensor2process.cep.model.events;

/**
 * Created by andreas on 11/9/16.
 */
public abstract class AbstractBusinessEvent implements IBusinessEvent {
    protected String id;

    public AbstractBusinessEvent(String id){
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
