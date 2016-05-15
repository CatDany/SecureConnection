package catdany.bfdist.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import catdany.bfdist.Helper;
import catdany.bfdist.log.SCLog;

public class ClientHandler implements Runnable
{
	public final Socket socket;
	public final int id;
	public final Server server;
	
	private Thread handlerThread;
	private BufferedReader in;
	private PrintWriter out;
	
	public ClientHandler(int id, Socket connectedClient, Server server)
	{
		this.id = id;
		this.socket = connectedClient;
		this.server = server;
		try
		{
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Helper.charset));
			this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Helper.charset), true);
			this.handlerThread = new Thread(this, "ClientHandler-" + id);
			handlerThread.start();
		}
		catch (Exception t)
		{
			SCLog.t(t);
			SCLog.w("Couldn't get I/O stream from client.");
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			String read;
			while ((read = in.readLine()) != null)
			{
				SCLog.d("Received message from client: %s", read);
			}
		}
		catch (Exception t)
		{
			SCLog.t(t);
			SCLog.w("Error occurred during client handling. Client dropped.");
			server.kick(this);
		}
	}
	
	/**
	 * Send a message to this client
	 * @param msg
	 */
	public void send(String msg)
	{
		out.println(msg);
		SCLog.d("Sent '%s' to client %s", msg, id);
	}
}