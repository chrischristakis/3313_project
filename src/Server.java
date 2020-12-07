import java.util.Scanner;

public class Server 
{

	public static void main(String[] args) 
	{
		Scanner scanner;

		ServerThread spawner = new ServerThread();
		spawner.start();
		while(true) //Constantly wait for users to connect. Terminates when server host types 'done'
		{
			scanner = new Scanner(System.in);
			String input = scanner.next();
			if(input.equalsIgnoreCase("done")) break;			
		}
		
		scanner.close();
		spawner.close();
		System.out.println("Terminating.");
	}
	
}
