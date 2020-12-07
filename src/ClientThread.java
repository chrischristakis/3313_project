import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ClientThread implements Runnable 
	{

		private Thread t;
		private ServerSocket ss;
		private Semaphore sem;
		private Socket socket;
		public ClientThread(ServerSocket ss, Semaphore sem)
		{
			this.ss = ss;
			this.sem = sem;
			t = null;
		}
		
		@Override
		public void run() 
		{
			System.out.println("Created client thread.");
			try 
			{
				socket = ss.accept();
				sem.release(); //allow server to establish another connection.
				
				InputStream input = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				while(true)
				{
					try
					{
						String line = reader.readLine();
						System.out.println(line);
					} catch(IOException e)
					{
						close();
					}
				}
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		public void start()
		{
			if(t != null) return;
			
			t = new Thread(this);
			t.start();
		}
		
		public void close()
		{
			if(socket == null || t == null) return;
			try
			{
				//gracefully end the socket and close the thread.
				socket.close();
				sem.release(); //Don't hog up the semaphore, return the wait.
				System.out.println("Closed client.");
				t.join();
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
	}