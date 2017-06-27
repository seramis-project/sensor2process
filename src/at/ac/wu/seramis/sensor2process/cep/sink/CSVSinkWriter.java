package at.ac.wu.seramis.sensor2process.cep.sink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CSVSinkWriter
{
	private static String _OUTPUT_PATH = "sinkMisplacedMovement.csv";

	private static CSVSinkWriter _instance;
	private static int _instanceCount = 0;

	private FileOutputStream csv;
	private BufferedWriter csvWriter;

	public CSVSinkWriter(String header, String path)
	{
		try
		{
			this.csv = new FileOutputStream(new File(path));
			_OUTPUT_PATH = path;
		}
		catch (Exception e)
		{
			System.err.println("Could not open file \"" + path + "\".");
			e.printStackTrace();
		}

		this.csvWriter = new BufferedWriter(new OutputStreamWriter(this.csv));

		this.writeLine(header);
	}

	public synchronized void writeLine(String... cells)
	{
		String line = "";

		for (String cell : cells)
		{
			line += cell + ";";
		}

		line = line.substring(0, line.length() - 1);

		try
		{
			this.csvWriter.write(line);
			this.csvWriter.newLine();
		}
		catch (IOException e)
		{
			System.err.println("Could not write to CSV File \"" + _OUTPUT_PATH + "\".");
			e.printStackTrace();
		}
	}

	public void close()
	{
		_instanceCount--;

		if (_instanceCount <= 0)
		{
			try
			{
				_instance.csvWriter.close();
				_instance.csv.close();
			}
			catch (IOException e)
			{
				System.err.println("Could not close \"" + _OUTPUT_PATH + "\".");
				e.printStackTrace();
			}
		}
	}

	public static CSVSinkWriter getInstance(String header, String path)
	{
		if (_instance == null)
		{
			synchronized (CSVSinkWriter.class)
			{
				if (_instance == null)
				{
					_instance = new CSVSinkWriter(header, path);
				}
			}
		}

		_instanceCount++;
		return _instance;
	}
}
