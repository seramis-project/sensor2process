package at.ac.wu.seramis.sensor2process.utils;

import at.ac.wu.seramis.sensor2process.cep.model.events.TemporalPosition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class CEPConnector
{
	private static ObservableMap<String, TemporalPosition> _temporalPositions = FXCollections.synchronizedObservableMap(FXCollections.observableHashMap());

	public static ObservableMap<String, TemporalPosition> getTemporalPositions()
	{
		return _temporalPositions;
	}
}
