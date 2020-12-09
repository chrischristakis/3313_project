import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Semaphore;

public class ClientThread implements Runnable 
	{

		private Thread t;
		private ServerSocket ss;
		private Semaphore sem;
		private Socket socket;
		private PrintWriter writer;
		BufferedReader reader;
		private ServerThread server;
		private String username;
		public ClientThread(ServerThread server, ServerSocket ss, Semaphore sem)
		{
			this.ss = ss;
			this.sem = sem;
			this.server = server;
			t = null;
		}
		
		@Override
		public void run() 
		{
			try 
			{
				socket = ss.accept();
				sem.release(); //allow server to establish another connection.
				
				InputStream input = socket.getInputStream();
				reader = new BufferedReader(new InputStreamReader(input));
				
				//For output of messages
				OutputStream output = socket.getOutputStream();
	            writer = new PrintWriter(output, true);
	            
	            //First input will be the username.
	            String username = reader.readLine();
	            if(username == null || server.usernames.contains(username)) //make sure username exists and isnt in use.
	            {	
	            	reader.readLine();
		            socket.close();
	            	return;
				}
	            else
	            {
	            	this.username = username;
	            	server.usernames.add(username);
	            }

	            server.broadcast(this.username + " has joined!");
				while(server.running)
				{
					try
					{
						String line = reader.readLine();
						if(line == null) //client has disconnected.
							throw new SocketException();
						server.broadcast(line);
						
					} catch(IOException e)
					{
						//This means client disconnected.
						server.broadcast(this.username+ " has left!");
					
						break;
					}
				}
				socket.close(); //clean up.
				
			} catch(SocketException e) 
			{
			
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			finally {
				if(server.usernames.contains(username))
					server.usernames.remove(username);
			}
		}
		
		public void start()
		{
			if(t != null) return;
			
			t = new Thread(this);
			t.start();
		}
		
		//Send a stirng to the user
		public void send(String message) {
			if(writer != null)
				writer.println(message);
		}
		
		public void close()
		{
			if(t == null) return;
			try
			{
				//gracefully end the socket and close the thread.
				if(socket != null) 
					socket.close();
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