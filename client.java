import java.util.*;
import java.net.*;
import java.io.*;

public class client {

	public static void main(String[] args) {
		try {
			//connecting to server
			Socket connectionToServer = new Socket("localhost", 1990);

			//get data from user
			Scanner userInput = new Scanner(System.in);
			System.out.println("Enter input");
			String input= userInput.nextLine();

			//send data to server
			PrintStream p = new PrintStream(connectionToServer.getOutputStream());
			p.println(input);

			//receive data from server
			Scanner recievedFromServer = new Scanner (connectionToServer.getInputStream());
			String temp = recievedFromServer.nextLine();
			System.out.println(temp);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
