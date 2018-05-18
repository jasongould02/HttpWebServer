package jcg.java.server.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import jcg.java.server.ClientWorker;

public class ThreadedServerTest {

	private ServerSocket server;
	ArrayList<ClientWorker> workers; // This is a bad way to handle the threads, should swap to pooling to be more efficient
	// If the server is alive it will continue to accept new socket connections
	private boolean alive = true;
	
	// Server Properties
	private final String SITE_PATH; // The path to the folder which holds the site. No other files should be accessed by the user(s)
	private final int PORT_NUMBER;
	private final int BACK_LOG;
	//private final int MAX_CLIENTS; //Change to max threads? To be replaced by MAX_THREADS
	//private final int MAX_THREADS;
		
	JFrame frame;
	JButton button;
	
	public static void main(String[] args) {
		try {
			ThreadedServerTest t = new ThreadedServerTest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public ThreadedServerTest() throws IOException {
		PORT_NUMBER = 2000;
		SITE_PATH = "site/";
		BACK_LOG = 100;
		//MAX_CLIENTS = 0;
		//MAX_THREADS = 0;
			
		File sitePath = new File(SITE_PATH);
		System.out.println("Site files located at: " + sitePath.getAbsolutePath());
		
		System.out.println("Creating window.");
		createWindow();
		workers = new ArrayList<ClientWorker>();
		//System.out.println("\t\t\t\t\t\t\t::\tStarting HttpServer\t::\t\n");
		System.out.println("Starting server.");
		server = new ServerSocket(PORT_NUMBER, BACK_LOG);
		// TODO: Instantiating new threads is too costly must change!
		while(isServerAlive()) {
			ClientWorker w = new ClientWorker(server.accept());
			w.start();
			workers.add(w);
		}
		System.out.println("Shutting Down.");
		for(ClientWorker w : workers) {
			w.closeSocket();
			w.stop();
		}
		server.close();
	}
		
	// TODO: Create Server window to manage connections? Maybe even manually send files or headers?
	private void createWindow() {
		button = new JButton();
		button.setText("Shutdown Server");
		button.addActionListener(new ActionListener() {
			@Override
		public void actionPerformed(ActionEvent e) {
				alive = false;
				System.out.println("SHUTTING DOWN THE SERVER");
				//frame.dispose();
				System.exit(0);
			}
		});
		frame = new JFrame();
		frame.setSize(200, 200);
		frame.setTitle("Web Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(button);
		frame.setVisible(true);
	}
	
	private boolean isServerAlive() {
		return alive;
	}
}
