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
//			System.out.println("Enter input");
//			String input= userInput.nextLine();


			//get username and password
			System.out.println("Enter a username");
			String username = userInput.nextLine();
			System.out.println("Enter a password");
			String password = userInput.nextLine();

			System.out.println("username: " + username + " password: " + password);


			//send data to server
			PrintStream p = new PrintStream(connectionToServer.getOutputStream());
			p.println(username + password);

			//receive data from server
			Scanner recievedFromServer = new Scanner (connectionToServer.getInputStream());
			String temp = recievedFromServer.nextLine();
			System.out.println(temp);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
