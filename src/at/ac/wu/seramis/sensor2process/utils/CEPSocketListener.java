package at.ac.wu.seramis.sensor2process.utils;

import at.ac.wu.seramis.sensor2process.cep.model.events.PositionEvent;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CEPSocketListener extends Thread
{
	private ServerSocket serverSocket;

	public CEPSocketListener()
	{
		try
		{
			this.serverSocket = new ServerSocket(1338);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		Socket clientSocket = null;
		try
		{
			clientSocket = this.serverSocket.accept();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		while(true)
		{
			try
			{
				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				PositionEvent pe = (PositionEvent) ois.readObject();

				// InputStream is = clientSocket.getInputStream();
				//
				// byte[] buffer = new byte[65000];
				//
				// while(is.read(buffer) != -1)
				// {
				// ;
				// }
				//
				// TemporalPosition tp = SerializationUtils.deserialize(buffer);

				System.out.println(pe);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
