package at.ac.wu.seramis.sensor2process.cep.model.events;

import java.io.Serializable;

/**
 * Created by andreas on 11/7/16.
 *
 * Temporal position of an actor. Position is stored in a 3D-coordinate system.
 * The z-axis can often
 * be ignored depending on the application.
 *
 */
public interface TemporalPosition extends Serializable
{

	String getId();

	double getX();

	double getY();

	double getZ();

	long getTimestamp();

	double getCertainty();

	void setId(String id);

	void setX(double x);

	void setY(double y);

	void setZ(double z);

	void setTimestamp(long timestamp);

	void setCertainty(double certainty);
}
