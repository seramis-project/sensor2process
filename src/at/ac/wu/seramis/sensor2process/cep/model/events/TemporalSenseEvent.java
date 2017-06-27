package at.ac.wu.seramis.sensor2process.cep.model.events;

/**
 * Created by andreas on 11/7/16.
 */
public interface TemporalSenseEvent {

    void setId(String id);

    void setX(double x);
    void setY(double y);
    void setZ(double z);

    void setSignalStrength(double signalStrength);
    void setTimestamp(long timestamp);


    String getId();

    double getX();
    double getY();
    double getZ();

    double getSignalStrength();
    long getTimestamp();
}
