import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	
	public boolean running = true;
	
	public Client() {
		try {
			BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in)); 
			
			int port;
			
			System.out.println("Please enter an IP to connect to: ");
			String ip = consoleReader.readLine();
			
			//get port input.
			while(true)
			{
				
				
				System.out.println("Please enter a port to connect to: ");
				try {
					String input = consoleReader.readLine();
					if(input.equalsIgnoreCase("done"))
						return; //exit program.
					port = Integer.parseInt(input);
					break;
				} catch(NumberFormatException e)
				{
					System.out.println("Enter a valid port.");
				}
			}
			
			Socket socket;
			try 
			{
				socket=new Socket(ip,port);
			} 
			catch(IOException e)
			{
				System.out.println("Can't connect to server on port " + port);
				return;
			}
			
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			
			ClientReader readerThread = new ClientReader(this, socket);
			readerThread.start();
			
			//Ask user for a name.
			System.out.println("Welcome to the 3313 chat app. You can enter 'done' to leave at any time.");
			String username = null;
			while(true)
			{
				System.out.print("Please enter a valid username: ");
				String msg = consoleReader.readLine();
				
				if(msg.equalsIgnoreCase("done"))
				{
					running = false;
					break;
				}
				
				if(msg == null || msg.length() == 0 || msg.equalsIgnoreCase("Admin"))
					System.out.println("Invalid name entered.");
				else
				{
					username = msg;
					writer.println(username);
					System.out.println("Connected to server as " + username);
					break;
				}
			}
			
			while(running)
			{
				String msg = consoleReader.readLine();
				if(msg.equalsIgnoreCase("done")) break;
				writer.println("[" + username + "]: " + msg);
			}
			consoleReader.close();
			socket.close();
			readerThread.close();
			System.out.println("Terminated.");
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public static void main(String[] args) {
		new Client();
	}
}
