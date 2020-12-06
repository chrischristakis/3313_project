import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
	public static void main(String[] args) 
	{
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(6666);
			Socket socket=ss.accept();
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = reader.readLine();    // reads a line of text
			System.out.println(line);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
