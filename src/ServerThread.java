import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class ServerThread implements Runnable
	{
		
		private Thread t;
		ServerSocket ss;
		ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
		
		@Override
		public void run() {
			Semaphore sem = new Semaphore(1);
		
			try
			{
				ss = new ServerSocket(1024);
				while(true)
				{
					sem.acquire();
					clients.add(new ClientThread(ss,sem));
					clients.get(clients.size()-1).start();
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			} catch (InterruptedException e) {
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
			if(ss == null) return;
			try {
				for(int i = 0; i < clients.size(); i++)
					clients.get(i).close();
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
