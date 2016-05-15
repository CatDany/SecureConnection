package catdany.bfdist.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.bind.DatatypeConverter;

import catdany.bfdist.Helper;
import catdany.bfdist.log.SCLog;

public class ServerCom implements Runnable
{
	public final Socket socket;
	
	private Thread comThread;
	private BufferedReader in;
	private PrintWriter out;
	
	public ServerCom(Socket socket)
	{
		this.socket = socket;
		try
		{
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Helper.charset));
			this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Helper.charset), true);
			this.comThread = new Thread(this, "Client-ServerCom");
			comThread.start();
		}
		catch (IOException t)
		{
			SCLog.t(t);
			SCLog.exit("Couldn't get I/O from server.");
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
				SCLog.d("Received message from server: %s", read);
			}
		}
		catch (Exception t)
		{
			SCLog.t(t);
			SCLog.exit("Error occurred while communicating with server.");
		}
	}
	
	/**
	 * Send a message to server
	 * @param msg
	 */
	public void sendToServer(String msg)
	{
		out.println(msg);
		SCLog.d("Sent '%s' to server", msg);
	}
	
	/**
	 * Send a byte array to server (temporarily sends hexadecimal encoded byte-array)
	 * @param bytes
	 */
	public void sendToServer(byte[] bytes)
	{
		out.println(new String(bytes, Helper.charset));
		SCLog.d("Sent %s bytes to server:", bytes.length);
		SCLog.d(DatatypeConverter.printHexBinary(bytes));
	}
}
