package at.ac.wu.seramis.sensor2process.simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import at.ac.wu.seramis.sensor2process.cep.model.events.ToryPositionEvent;
import at.ac.wu.seramis.sensor2process.database.DataAccess;

public class CSVBuffer extends Buffer
{

	private String csv;

	public CSVBuffer(String csv)
	{
		this.csv = csv;
	}

	@Override
	public void run()
	{

		// String csvFile = "data/experiment/LastWeekEG.csv";
		String csvFile = "../../Data/Adler/Erfurt/misplaced/Tory 20161207.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
//		HashMap<String, Integer> classesMap = DataAccess.getEPCClassesMap();
		try
		{

			br = new BufferedReader(new FileReader(this.csv));
			// ignore header row
			br.readLine();

			while ((line = br.readLine()) != null)
			{
				// use comma as separator
				String[] row = line.split(cvsSplitBy);

				ToryPositionEvent event = new ToryPositionEvent();
				event.setId(row[0]);
				event.setItemClass(row[3]);
				event.setX(Double.parseDouble(row[4]));
				event.setY(Double.parseDouble(row[5]));
				event.setCertainty(Double.parseDouble(row[6]));

				this.buffer.add(event);

			}

			this.buffer.add(null);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

	}
}
