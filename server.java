import java.util.Scanner;
import java.net.*;
import java.io.*;

public class server {

	public static void main(String[] args) {
		try {
			//start server
			ServerSocket server = new ServerSocket(1990);
			System.out.println("Server is running");
			
			//accept client
			Socket client = server.accept();
			System.out.println("Client has connected to server");		
		
			//recieve data from client
			Scanner clientData = new Scanner(client.getInputStream());
			String temp;
			temp = clientData.nextLine();

			//sent data to client
			PrintStream p = new PrintStream(client.getOutputStream());
			p.println(temp);

			} catch(IOException e) {
			System.out.println(e);
		}
	}
}
