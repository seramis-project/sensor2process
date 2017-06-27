package at.ac.wu.seramis.sensor2process.cep.model.patterns.location;

/**
 * Created by andreas on 11/18/16.
 */
public abstract class Shape implements ILocation {
    private static final long serialVersionUID = 2352379190325323749L;

    @Override
    public String getName() {
        return "unnamed";
    }
}
