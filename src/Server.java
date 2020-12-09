import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Server 
{

	static ArrayList<Integer> ports = new ArrayList<Integer>();
	
	public static void main(String[] args) 
	{
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in)); 
		
		ArrayList<ServerThread> spawners = new ArrayList<ServerThread>();
		
		while(true) //Constantly wait for users to connect. Terminates when server host types 'done'
		{
			String input;
			try {
				System.out.println("Enter a port to create a new server, or 'done' to terminate.");
				input = consoleReader.readLine();
				if(input.equalsIgnoreCase("done")) break;	
				
				//Make usre valid input is added.
				try {
					int port = Integer.parseInt(input);
					
					if(ports.contains(port))
						System.out.println("Port already in use.");
					else
					{
						spawners.add(new ServerThread(port));
						spawners.get(spawners.size()-1).start();
						ports.add(port);
						System.out.println("Server online.");
					}
				} catch(NumberFormatException e)
				{
					System.out.println("Enter a valid input.");
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			consoleReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(ServerThread spawner: spawners)
			spawner.close(); //gracefully terminate.
		System.out.println("Terminating.");
	}
	
}
