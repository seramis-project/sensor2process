package at.ac.wu.seramis.sensor2process.simulation;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.collections.ObservableList;

public class DataStreamSimulator
{
	public static final double FAST_FORWARD_FACTOR = 2.0;

	public static void main(String[] args)
	{
		Buffer dbBuffer = new DatabaseBuffer(0, 0);
		// Buffer dbBuffer = new TestBuffer();
		ObservableList<Object> buffer = dbBuffer.getBuffer();

		/*
		 * boolean first = true;
		 * buffer.addListener((ListChangeListener<String>) c ->
		 * {
		 * System.out.println(buffer.get(0) + " (" + buffer.size() + ")");
		 * buffer.remove(0);
		 * });
		 */

		DatagramSocket cepServer = null;
		InetAddress cepServerAddress = null;
		try
		{
			cepServer = new DatagramSocket();
			cepServerAddress = InetAddress.getByName("localhost");
			System.out.println(cepServerAddress);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		dbBuffer.start();

		long timediff = 0;
		// boolean first = true;

		while (true)
		{
			if (buffer.size() > 0)
			{
				RawRead entry = (RawRead) buffer.get(0);

				// calculate initial difference between now and first data entry
				if (timediff == 0)
				{
					timediff = ChronoUnit.MILLIS.between(entry.getTimestamp(), LocalDateTime.now());
				}

				if (ChronoUnit.MILLIS.between(entry.getTimestamp(), LocalDateTime.now()) * FAST_FORWARD_FACTOR >= timediff)
				{
					// first = true;
					byte[] data = (entry).toString().getBytes();
					try
					{
						cepServer.send(new DatagramPacket(data, data.length, cepServerAddress, 1337));
						System.out.println("SENT " + entry.getTimestamp());
						buffer.remove(0);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					// if(first)
					// {
					// System.out.println("WAITING for " +
					// entry.getTimestamp());
					// first = false;
					// }

					// wait until time of next entry is reached
					try
					{
						Thread.sleep(100);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			else
			{
				// wait until buffer has entries
				try
				{
					Thread.sleep(100);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
