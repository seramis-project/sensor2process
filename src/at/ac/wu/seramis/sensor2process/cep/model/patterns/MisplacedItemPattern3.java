package at.ac.wu.seramis.sensor2process.cep.model.patterns;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.flink.cep.pattern.Pattern;

import at.ac.wu.seramis.sensor2process.cep.model.events.IBusinessEvent;
import at.ac.wu.seramis.sensor2process.cep.model.events.MisplacedItem;

public class MisplacedItemPattern3 extends AbstractBusinessEventPattern<IBusinessEvent>
{
	private static final long serialVersionUID = 1L;

	private static ArrayList<String> _misplacedCSV = new ArrayList<String>();

	static
	{
		_misplacedCSV = loadMisplaced();

	}

	public MisplacedItemPattern3()
	{

	}

	private static ArrayList<String> loadMisplaced()
	{
		// TODO Auto-generated method stub
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";
		ArrayList<String> misplaced = new ArrayList<String>();
		try
		{

			br = new BufferedReader(new FileReader("resources/misplaced/misplaced_groups_dite.csv"));
			// ignore header row
			br.readLine();
			while ((line = br.readLine()) != null)
			{
				// use comma as separator
				String[] row = line.split(cvsSplitBy);
				misplaced.add(row[6]);
				// System.out.println(row[6]);
			}
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
		return misplaced;
	}

	@Override
	public MisplacedItem create(Map<String, IBusinessEvent> pattern)
	{
		MisplacedItem misplacedItem = (MisplacedItem) pattern.get("misplacedItem");
		misplacedItem.setMisplaced(_misplacedCSV.contains(misplacedItem.getId()));
		return misplacedItem;
	}

	@Override
	public Pattern<IBusinessEvent, MisplacedItem> getEventPattern()
	{
		return Pattern.<IBusinessEvent> begin("misplacedItem").subtype(MisplacedItem.class);
		// .where(evt ->
		// {
		//
		// return _misplacedCSV.contains(evt.getId());
		//
		// });
	}

	@Override
	public String getPatternCode()
	{
		return "misplacedItemPositive()";
	}

}
