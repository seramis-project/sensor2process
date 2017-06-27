package at.ac.wu.seramis.sensor2process.database.source;

import at.ac.wu.seramis.sensor2process.database.model.RawRead;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Buffer extends Thread
{
	protected ObservableList<RawRead> buffer = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

	public ObservableList<RawRead> getBuffer()
	{
		return this.buffer;
	}

}
