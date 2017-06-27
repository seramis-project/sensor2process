package at.ac.wu.seramis.sensor2process.cep.model.patterns.location;


import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by andreas on 11/17/16.
 */
@Root
public interface ILocation extends Serializable {
    boolean contains(Point point);
    String getName();
}
