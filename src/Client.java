import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public static void main(String[] args) {
		try {
			Socket socket=new Socket("localhost",6666);
			System.out.println("Connected to server.");
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println("This is a message sent to the server");
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
}
