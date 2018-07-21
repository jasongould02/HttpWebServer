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
import jcg.java.server.ServerProperties;
import jcg.java.server.util.logging.Log;

public class ThreadedServerTest {

	private ServerSocket server;
	ArrayList<ClientWorker> workers; // This is a bad way to handle the threads, should swap to pooling to be more efficient
	// If the server is alive it will continue to accept new socket connections
	private boolean alive = true;
	
	private final String SITE_PATH; // The path to the folder which holds the site. No other files should be accessed by the user(s)
	private final int PORT_NUMBER;
	private final int BACK_LOG;
	//private final int MAX_CLIENTS; //Change to max threads? To be replaced by MAX_THREADS
	//private final int MAX_THREADS;
		
	JFrame frame;
	JButton button;
	
	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			ThreadedServerTest t = new ThreadedServerTest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public ThreadedServerTest() throws IOException {
		Log log = new Log();
		log.start();
		// TODO: remove these, replace all calls to get these vars to use ServerProperties
		PORT_NUMBER = Integer.parseInt(ServerProperties.getProperty("PORT_NUMBER"));
		SITE_PATH = ServerProperties.getProperty("SITE_PATH");
		BACK_LOG = Integer.parseInt(ServerProperties.getProperty("BACK_LOG"));
		//MAX_CLIENTS = 0;
		//MAX_THREADS = 0;
			
		File sitePath = new File(SITE_PATH);
		Log.log("Site files located at: " + sitePath.getAbsolutePath());
		
		Log.log("Creating window");
		createWindow();
		workers = new ArrayList<ClientWorker>();
		Log.log("Starting server @ Port: " +  PORT_NUMBER);
		server = new ServerSocket(PORT_NUMBER, BACK_LOG);
		// TODO: Instantiating new threads is too costly must change!
		while(isServerAlive()) {
			ClientWorker w = new ClientWorker(server.accept());
			w.start();
			workers.add(w);
		}
		Log.log("Server is no longer alive");
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
				Log.log("[GUI SHUTDOWN] Shutting down server");
				
				try {
					server.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
