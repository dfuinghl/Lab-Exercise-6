import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient
{
	public static void main (String []args)
	{
		int id = Integer.parseInt(args[0]);
		new ChatClient(id).run();
	}
	private static final int DEFAULT_PORT = 54321;
	private static final String hostName = "localhost";
	private ObjectInputStream responseStream;
	private ObjectOutputStream requestStream;
	private InetAddress ina;
	private Socket s;
	private Random random;
	private int counter;
	private boolean status;
	private int id;

	public ChatClient(int id)
	{
		try{
			ina  = InetAddress.getByName(hostName);
		}catch (UnknownHostException u)
		{
			System.out.println("Cannot find host name");
			System.exit(0);
		}
		try{
			s = new Socket(ina, DEFAULT_PORT);
		}catch (IOException ex)
		{
			System.out.println("Cannot connect to host");
			System.exit(1);
		}

		try{
			responseStream = new ObjectInputStream(s.getInputStream());
			requestStream = new ObjectOutputStream(s.getOutputStream());
			requestStream.flush();
		}catch (IOException io)
		{
			System.out.println("Failed to get socket streams");
			System.exit(1);
		}
		random = new Random();
		this.counter = 10;
		this.status = true;
		this.id = id;
	}

	private Transmitter tx;
	private Receiver rec;
	private boolean txEnded;
	private boolean recEnded;

	public synchronized void checkBothEnded()
	{
		if (txEnded&&recEnded)
		{
			try{
				requestStream.close();
				responseStream.close();
				s.close();
			}catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void run()
	{
		System.out.println("Starting Chat Client "+id);
		System.out.println("Connecting to Server");
		txEnded = false;
		recEnded = false;
		tx = new Transmitter();
		tx.start();
		rec = new Receiver();
		rec.start();
	}
	private class Transmitter extends Thread
	{
		public void run()
		{
				Message message = null;
				String msg = Keyboard.readString("Input a positive Integer: ");
				try{
					Thread.sleep(random.nextInt(1000));
					message = new Message("check" ,msg, id);
					requestStream.writeObject(message);
					requestStream.flush();
					requestStream.reset();
				}catch (InterruptedException e)
				{
					e.printStackTrace();
				}catch (IOException e)
				{
					e.printStackTrace();
				}


			txEnded = true;
			checkBothEnded();
		}
	}
	private class Receiver extends Thread
	{
		public void run()
		{
				try{
					Message response = (Message) responseStream.readObject();

					if (response.getControl().equals("notpositive"))
					{
						System.out.println("Positive Integer required");
						System.out.println("Connection closed");
					}
					else if (response.getControl().equals("notprime"))
					{
						System.out.println("Not Prime");
						System.out.println("Connection closed");
					}
					else if (response.getControl().equals("prime"))
					{
						System.out.println("Is Prime");
						System.out.println("Connection closed");
					}

				}catch (IOException e)
				{
					e.printStackTrace();
				}catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}

			recEnded = true;
			checkBothEnded();
		}
	}

}
