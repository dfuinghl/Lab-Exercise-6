import java.io.*;
import java.net.*;

public class ChatServerThread implements Runnable
{
	private Thread threadedServer;
	private Socket sock;
	private Broadcaster broadcaster;
	private ObjectInputStream requests;
	private ObjectOutputStream responses;
	private boolean closeFlag;
	private Prime p;

	public ChatServerThread(Socket sock, Broadcaster broadcaster)
	{
		this.sock = sock;
		this.broadcaster = broadcaster;
		this.closeFlag = false;
		p = new Prime();

		try{
			responses = new ObjectOutputStream(sock.getOutputStream());
			responses.flush();
			requests = new ObjectInputStream(sock.getInputStream());
		}catch (IOException ie)
		{
			System.out.println("Cannot open stream");
			try{
				sock.close();
			}catch (Exception e)
			{
				System.exit(0);
			}

		}
		threadedServer = new Thread(this);
		threadedServer.start();
	}

	public void run()
	{
		Message message = null;

			try{
				message = (Message) requests.readObject();
				String y = message.getAssociatedData();
				int x = Integer.parseInt(y);

			}catch (NumberFormatException ne)
			{
				message.setControl("notpositive");
				broadcaster.newMessage(message);
				System.out.println("NF issue");
				System.out.println("Connection will be closed");
				System.exit(0);
			}
			catch (Exception e)
			{
				System.out.println("Exception caught");
			}
		String y = message.getAssociatedData();
		int x = Integer.parseInt(y);

		if (x <= 0)
		{
			message.setControl("notpositive");
			broadcaster.newMessage(message);

		}
		else if (p.isPrime(x))
		{
			message.setControl("prime");
			broadcaster.newMessage(message);
		}
		else if (!(p.isPrime(x)))
		{
			message.setControl("notprime");
			broadcaster.newMessage(message);
		}


		System.out.println("Remove chat client " +message.getId()+" from broadcaster");
		broadcaster.removeClient(this);

		try{
			responses.close();
			requests.close();
			sock.close();
		}catch (Exception eclose){}
		System.out.println("Server closing");
		System.exit(0);
	}

	public void sentMessage (Message message)
	{
		try{
			responses.writeObject(message);
			responses.flush();
			responses.reset();
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}



}
