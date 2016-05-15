package catdany.bfdist.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import catdany.bfdist.Main;
import catdany.bfdist.log.SCLog;

public class Server implements Runnable
{
	private static Server instance;
	
	public final int port;
	private ServerSocket socket;
	private Console console = new Console(this);
	private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	private Thread serverThread;
	
	private Server(int port)
	{
		this.port = port;
		try
		{
			socket = new ServerSocket(port);
			SCLog.i("Bound to port %s.", port);
		}
		catch (IOException t)
		{
			SCLog.t(t);
			SCLog.exit("Unable to bind to port %s.", port);
		}
		catch (IllegalArgumentException t)
		{
			SCLog.t(t);
			SCLog.exit("Port %s is out of range (0-65535).", port);
		}
		this.serverThread = new Thread(this, "Server");
		serverThread.start();
	}
	
	/**
	 * Creates a new instance of {@link Server} for given port.<br>
	 * Saves it in {@link Server#instance}, so you can refer to it later.
	 * @param port
	 * @return
	 * @see Server#getServer()
	 */
	public static Server instantiate(int port)
	{
		instance = new Server(port);
		return instance;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Socket connectedClient = socket.accept();
				int id = clients.size();
				ClientHandler ch = new ClientHandler(id, connectedClient, this);
				clients.add(ch);
				SCLog.i("Client accepted (id: %s, from: %s)", id, connectedClient.getRemoteSocketAddress().toString());
			}
			catch (IOException t)
			{
				SCLog.t(t);
				SCLog.w("Client couldn't connect.");
			}
		}
	}
	
	/**
	 * Get {@link Server} instance.<br>
	 * Will throw an exception if called on client-side.
	 * @return
	 * {@link Server} instance if called on server-side<br>
	 * <code>null</code> if called on client-side
	 * @see Server#instantiate(InetAddress, int)
	 * @return
	 */
	public static Server getServer()
	{
		if (Main.getSide().isServer())
		{
			return instance;
		}
		else
		{
			SCLog.t("getServer() called on client-side");
			return null;
		}
	}
	
	/**
	 * Send a message to all connected clients
	 * @param msg
	 */
	public void sendToAll(String msg)
	{
		for (ClientHandler i : clients)
		{
			i.send(msg);
		}
	}
	
	/**
	 * Kick connected client
	 * @param client
	 */
	public void kick(ClientHandler client)
	{
		if (clients.contains(client))
		{
			clients.remove(client);
			SCLog.d("Client kicked (%s)", client.id);
		}
		else
		{
			SCLog.d("Attempted to remove a client that is not connected.");
		}
	}
	
	/**
	 * Get {@link Console} object
	 * @return
	 */
	protected Console getConsole()
	{
		return console;
	}
	
	/**
	 * Get all connected clients<br>
	 * <b>Warning:</b> use {@link ArrayList#clone()} you intend to modify it
	 * @return
	 */
	protected ArrayList<ClientHandler> getClients()
	{
		return clients;
	}
}