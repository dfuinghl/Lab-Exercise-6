import java.io.*;
import java.net.*;

public class ChatServer
{
	private static final int DEFAULT_PORT = 54321;
	private Broadcaster broadcaster;

	public static void main(String[] args)
	{
		new ChatServer().run();
	}
	public ChatServer()
	{
		broadcaster = new Broadcaster();
	}
	public void run()
	{
		int port = DEFAULT_PORT;

		ServerSocket reception_socket = null;
		try{
			reception_socket = new ServerSocket(port);
			System.out.println("Started server on port: "+port);
		}catch (IOException ie)
		{
			System.out.println("Cannot create server socket");
			System.exit(0);
		}

		for (;;)
		{
			Socket client_socket = null;

			try {
				client_socket = reception_socket.accept();
				System.out.println("Accepting request from " + client_socket.getInetAddress());
			}catch (IOException io)
			{
				System.out.println("Problem accepting client socket");
			}

			ChatServerThread client = new ChatServerThread (client_socket, broadcaster);
			broadcaster.addClient(client);
		}
	}
}
