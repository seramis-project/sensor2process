package at.ac.wu.seramis.sensor2process.cep.model.patterns.location;

import at.ac.wu.seramis.sensor2process.cep.model.events.PositionBusinessEvent;
import org.apache.commons.io.FileUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreas on 11/24/16.
 */
public class LocationRegistry {
    private static LocationRegistry _instance;

    private Map<String, ILocation> availableLocations;

    private LocationRegistry() {
        availableLocations = new HashMap<>();
        loadLocations();
    }

    private void loadLocations() {
        File locationsFolder = new File("./resources/locations");
        Serializer serializer = new Persister();
        if (locationsFolder.exists() && locationsFolder.isDirectory()){
            for (File locationDefinition : locationsFolder.listFiles()){
                try {
                    String locDef = FileUtils.readFileToString(locationDefinition, "utf-8");
                    ILocation location = null;
                    try {
                        location = serializer.read(CompositeLocation.class, locDef);
                    } catch (Exception e) {
                        try {
                            location = serializer.read(PlainLocation.class, locDef);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    this.availableLocations.put(location.getName(), location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static LocationRegistry getInstance(){
        if(_instance == null)
        {
            synchronized (LocationRegistry.class)
            {
                if(_instance == null)
                {
                    _instance = new LocationRegistry();
                }
            }
        }
        return _instance;
    }

    public String getTypeOfLocationThatContains(Point point){
        return getTypeOfLocationThatContains(point, PositionBusinessEvent.SALES_FLOOR_TYPE);
    }

    private String getTypeOfLocationThatContains(Point point, String defaultType) {
        for (String key : availableLocations.keySet()){
            if (availableLocations.get(key).contains(point)){
                return key;
            }
        }
        return defaultType;
    }
}
