package at.ac.wu.seramis.sensor2process.simulation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Buffer extends Thread
{
	protected ObservableList<Object> buffer = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

	public ObservableList<Object> getBuffer()
	{
		return this.buffer;
	}

}
