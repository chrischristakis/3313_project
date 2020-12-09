import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class ServerThread implements Runnable
	{
		
		private Thread t;
		ServerSocket ss;
		Semaphore sem;
		ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
		protected boolean running;
		private int port;
		public ArrayList<String> usernames = new ArrayList<String>();
		
		public ServerThread(int port) {
			this.port = port;
		}
		
		@Override
		public void run() {
			sem = new Semaphore(1);
			running = true;
			try
			{
				ss = new ServerSocket(port);
				while(true)
				{
					sem.acquire(); //Always have (clients+1) accept() functions running.
					if(running)
					{
						clients.add(new ClientThread(this,ss,sem));
						clients.get(clients.size()-1).start();
					}
					else
						break; //exit the loop. We do it this way since changing running while sem is wating won't prevent it from making a new thread.
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Send a message to all users
		public void broadcast(String msg) {
			for(int i = 0; i < clients.size(); i++)
				clients.get(i).send(msg);
		}
		
		public void start()
		{
			if(t != null) return;
			t = new Thread(this);
			t.start(); //
		}
		
		public void close()
		{
			if(ss == null) return;
			try {
				running = false;
				sem.release();
				ss.close(); 
				for(int i = 0; i < clients.size(); i++)
					clients.get(i).close();
				t.join(); //Close up the thread
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		
	}
