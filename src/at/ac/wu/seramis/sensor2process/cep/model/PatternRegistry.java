package at.ac.wu.seramis.sensor2process.cep.model;

import at.ac.wu.seramis.sensor2process.cep.model.patterns.*;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.CompositeLocation;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.ILocation;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.Named;
import at.ac.wu.seramis.sensor2process.cep.model.patterns.location.PlainLocation;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.InstantiationException;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.ValueRequiredException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreas on 11/18/16.
 */
public class PatternRegistry {

    private static final AbstractBusinessEventPattern[] patterns = new AbstractBusinessEventPattern[]{new MovingTrivialPattern(), new MovingConservativePattern(), new StartsMovingPattern(), new StopsMovingPattern()};

    private static PatternRegistry _instance;

    private Map<String, AbstractBusinessEventPattern> availablePatterns;
    private Map<String, AbstractBusinessEventPattern> availableLocationPatterns;
    private Object locationPatterns;

    private PatternRegistry(){
        availablePatterns = new HashMap<>();
        availableLocationPatterns = new HashMap<>();

        try {
            loadLocationPatterns();
            loadDynamicPatterns();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AbstractBusinessEventPattern[] getPatterns() {
        return patterns;
    }

    private void loadDynamicPatterns() {
        for (AbstractBusinessEventPattern pattern : patterns){
            availablePatterns.put(pattern.getPatternCode(), pattern);
        }
    }

    private void loadLocationPatterns() throws Exception {
        File[] files = new File("./resources/locations").listFiles();
        Serializer serializer = new Persister();
        for (File locFile : files){
            ILocation location;
            try{
                location = serializer.read(CompositeLocation.class, locFile);
            } catch (InstantiationException e){
                location = serializer.read(PlainLocation.class, locFile);
            } catch (ValueRequiredException e){
                location = serializer.read(PlainLocation.class, locFile);
            }
            String codeName = ((Named) location).getName();
            availableLocationPatterns.put(codeName, new LocationBusinessEventPattern(location, codeName));
        }
    }

    public static PatternRegistry getInstance(){
        if (_instance == null){
            synchronized(PatternRegistry.class){
                if (_instance == null){
                    _instance = new PatternRegistry();
                }
            }
        }
        return _instance;
    }

    public AbstractBusinessEventPattern get(String s) {
        return availablePatterns.get(s);
    }

    public Map<String, AbstractBusinessEventPattern> getLocationPatterns() {
        return availableLocationPatterns;
    }
}
