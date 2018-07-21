package jcg.java.server.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import jcg.java.server.ClientWorker;
import jcg.java.server.ServerProperties;
import jcg.java.server.window.Window;

public class ServerExample {

	Window window;
	private ServerSocket serverSocket;
	ArrayList<ClientWorker> workerList;
	
	public static void main(String[] args) {
		try {
			ServerExample se = new ServerExample();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ServerExample() throws IOException {
		int portNumber = Integer.parseInt(ServerProperties.getProperty("PORT_NUMBER"));
		serverSocket = new ServerSocket(portNumber);
		
		window = new Window("server example");
		workerList = new ArrayList<ClientWorker>();
		
	}
	
}
