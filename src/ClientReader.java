import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ClientReader implements Runnable
{
	
	private Socket socket;
	private Client client;
	private Thread t;
	private BufferedReader reader;
	
	public ClientReader(Client client, Socket socket) 
	{
		this.socket = socket;
		this.client = client;
	}

	//responsible for reading messages so the reading and writing aspects don't interfere in a single thread.
	@Override
	public void run() 
	{
		InputStream input;
		try {
			input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
			while(true) {
				String line = reader.readLine();
				if(line == null) 
					throw new SocketException();
				else
					System.out.println(line);
			}
		} catch(SocketException e)
		{
			System.out.println("SERVER: Socket interrupted.");
			client.running = false;
			//TODO close client.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		if(t != null) return;
		t = new Thread(this);
		t.start();
	}
	
	public void close() {
		if(t == null) return;
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
