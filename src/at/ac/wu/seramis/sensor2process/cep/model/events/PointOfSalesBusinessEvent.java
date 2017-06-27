package at.ac.wu.seramis.sensor2process.cep.model.events;

import at.ac.wu.seramis.sensor2process.cep.pgsql.model.BusinessEvent;

/**
 * Created by andreas on 11/17/16.
 */
public class PointOfSalesBusinessEvent extends PositionBusinessEvent {

//    public PointOfSalesBusinessEvent(BusinessEvent businessEvent) {
//        super(businessEvent, POINT_OF_SALES_TYPE);
//    }

    public PointOfSalesBusinessEvent(TemporalPosition tag) {
        super(tag, POINT_OF_SALES_TYPE);
    }
}
