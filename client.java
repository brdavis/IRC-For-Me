import java.net.*;
import java.io.*;

public class client {
	private static Socket connectionToServer;
	private static PrintWriter content;

	public static void main(String[] args) {
		try {
			connectionToServer = new Socket("localhost", 1990);
			content = new PrintWriter(connectionToServer.getOutputStream(), true);
			content.println("Testing 1, 2, 3");

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
