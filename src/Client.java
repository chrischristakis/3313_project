import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		try {
			Socket socket=new Socket("localhost",1024);
			System.out.println("Connected to server.");
			
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			
			Scanner scanner = new Scanner(System.in);
			while(true)
			{
				String msg = scanner.next();
				if(msg.equalsIgnoreCase("done")) break;
				writer.println(msg);
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
}
