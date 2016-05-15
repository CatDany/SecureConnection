package catdany.bfdist.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import catdany.bfdist.Main;
import catdany.bfdist.log.SCLog;

public class Client
{
	private static Client instance;
	
	/**
	 * Server communicator
	 */
	private ServerCom com;
	/**
	 * Server socket
	 */
	private Socket socket;
	
	public Client(InetAddress ip, int port)
	{
		connect(ip, port);
	}
	
	/**
	 * Creates a new instance of {@link Client} for given {@link InetAddress} and port.<br>
	 * Saves it in {@link Client#instance}, so you can refer to it later.
	 * @param ip
	 * @param port
	 * @see Client#getClient()
	 */
	public static void instantiate(InetAddress ip, int port)
	{
		instance = new Client(ip, port);
	}
	
	/**
	 * Connects to the server and creates a thread to keep this connection
	 * @param ip
	 * @param port
	 */
	public void connect(InetAddress ip, int port)
	{
		try
		{
			socket = new Socket(ip, port);
			this.com = new ServerCom(socket);
			SCLog.i("Connected to server: %s", socket.getRemoteSocketAddress().toString());
		}
		catch (IOException t)
		{
			SCLog.t(t);
			SCLog.exit("Unable to create client socket on client-side.");
		}
	}
	
	/**
	 * Get server communicator
	 * @return
	 */
	public ServerCom getServerCom()
	{
		return com;
	}
	
	/**
	 * Get {@link Client} instance.<br>
	 * Will throw an exception if called on server-side.
	 * @return
	 * {@link Client} instance if called on client-side<br>
	 * <code>null</code> if called on server-side
	 * @see Client#instantiate(InetAddress, int)
	 */
	public static Client getClient()
	{
		if (Main.getSide().isClient())
		{
			return instance;
		}
		else
		{
			SCLog.t("getClient() called on server-side");
			return null;
		}
	}
}