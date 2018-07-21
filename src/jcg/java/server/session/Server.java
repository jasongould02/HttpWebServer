package jcg.java.server.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jcg.java.server.util.logging.Log;

public class Server implements Runnable {

	private int blockID;
	private ServerSocket serverSocket;
	private BlockingQueue<Socket> socketList;
	private Thread thread;
	private boolean running = false;
	
	// Keeps it in local space
	Server() {
		socketList = new LinkedBlockingQueue<Socket>();
		Socket temp =  new Socket();
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		
	}
	
	public synchronized void start() {
		thread = new Thread(this, "ThreadPool.Size:" + poolSize + "@" + (System.currentTimeMillis() / 1000) + "s");
		running = true;
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
		//thread = null;
	}
	
	private void checkInputStream(Socket clientSocket) {
		String input;
		BufferedReader i;
		try {
			i = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
			/* Note: loop will not end once the readLine() character returns a blank, since the 
			 * InputStream is still waiting for communication from the socket.
			 * 
			 * If you add a clause to close the stream once it returns a blank character,
			 * it will end the entire connection.
			*/ 
			while((input = i.readLine()) != null) { // After reading the first line, read the rest of the input
				Log.log("REQUEST:" + input);
				//System.out.println("INPUT:: "+ input);
				if(input.startsWith("GET")) {
					Log.log("GET request from " + clientSocket.getRemoteSocketAddress().toString().replaceAll("/", " ").trim());
					String sep[] = input.split(" ");
					if(sep[0].equals("GET") && sep[2].equals("HTTP/1.1")) { // Only respond to HTTP/1.1
						// TODO: Set it to respond
						//response(o, input);
					}
				}
				// "close" instead of "closed" 
				if(input.toLowerCase().contains("connection") && input.toLowerCase().contains("close")) { //if(input.toLowerCase().contains("connection : close")) {
					break; // exit the loop and shut the server down!
				}
			}
			
			o.close();
			i.close();
	} catch(IOException e) {
		e.printStackTrace();
	}
	}

	public synchronized void stop() throws InterruptedException {
		/*for(Worker w : workerList) {
			w.stop();
		}*/
		thread.join();
		
		
		thread = null;
		running = false;
	}

	
}
