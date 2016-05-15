package catdany.bfdist.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import catdany.bfdist.log.SCLog;

public class Console implements Runnable
{
	public final Thread consoleThread;
	private Server server;
	
	public Console(Server server)
	{
		this.server = server;
		this.consoleThread = new Thread(this, "Server-Console");
		consoleThread.start();
	}
	
	@Override
	public void run()
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true)
		{
			try
			{
				String read = in.readLine();
				SCLog.logToFile("[SYSIN] " + read);
			}
			catch (IOException t)
			{
				SCLog.t(t);
				SCLog.e("Unable to read from console.");
			}
		}
	}
}