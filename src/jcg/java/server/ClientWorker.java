package jcg.java.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import jcg.java.server.test.ThreadedServerTest;
import jcg.java.server.util.FileUtil;
import jcg.java.server.util.logging.Log;

public class ClientWorker implements Runnable {

	private Thread thread;
	private String remoteIP;
	private boolean running = false;
	
	private Socket clientSocket;
	
	public ClientWorker(Socket cs) {
		this.clientSocket = cs;
		remoteIP = clientSocket.getRemoteSocketAddress().toString().replaceAll("/", " ").trim();
	}
	
	@Override
	public void run() {
		String input;
		PrintWriter o;
		BufferedReader i;
		try {
			o = new PrintWriter(clientSocket.getOutputStream());
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
					Log.log("GET request from " + remoteIP);
					String sep[] = input.split(" ");
					if(sep[0].equals("GET") && sep[2].equals("HTTP/1.1")) { // Only respond to HTTP/1.1 
						response(o, input);
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
		
		
		try {
			// Close socket
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void closeSocket() {
		try {
			// Closing the socket should close its streams
			/*clientSocket.getInputStream().close();
			clientSocket.getOutputStream().close();*/
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: Change so the entire HTTP Request header is sent to this function?
	private void response(PrintWriter out, String line) throws IOException {
		String[] sep = line.split(" ");
		File file;
		
		//if(file.getParentFile() == null || file.getParentFile().getName() != ServerProperties.getProperty("SITE_PATH")) {
		if(!sep[1].trim().startsWith("/" + ServerProperties.getProperty("SITE_PATH"))) {
			Log.log("Invalid parent file (Requested file not located in specified SITE_PATH)");
			out.write("HTTP/1.1 403 Forbidden" + "\r\n");
			out.write("Connection: close" + "\r\n");
			//out.write("Content-Length: " + 0 + "\r\n");
			out.write("\r\n");
			out.flush();
			return;
		}
		
		
		// This is because HTTP send requests as: *RequestType* /*File* *HTTPVer*
		if(sep[1].startsWith("/")) {
			sep[1] = sep[1].replaceFirst("/", " ").trim();
			if(sep[1].length() <= 0) {
				file = new File("index.html");
			} else {
				file = new File(sep[1]);
			}
		} else {
			file = new File(sep[1]);
		}
		
		/*Log.log("Getting FILE: " + file.getName());
		Log.log("Getting FILEPARENT: " + file.getParentFile().getName());*/
		
		
		String fileExtension = FileUtil.getFileExtension(file);
		//System.out.println("Sending file: " + file.getName() + " With Extension: " + fileExtension); //TODO: Replace with log statement
		
		if(!file.exists() || FileUtil.getFileExtension(file) == null) {
			// TODO HTTP 404 File Not Found
			out.write("HTTP/1.1 404 OK" + "\r\n");
			out.write("Connection: close" + "\r\n");
			//out.write("Content-Length: " + 0 + "\r\n");
			out.write("\r\n");
		} else {
			// TODO check if file is allowed to be sent/accessed
			out.write("HTTP/1.1 200 OK" + "\r\n");
			out.write("Content-Type: text/" + fileExtension + "\r\n");
			out.write("Connection: keep-alive" + "\r\n");
			out.write("Content-Length: " + FileUtil.getFileContentLength(file) + "\r\n");
			out.write("\r\n");
			out.write(FileUtil.getFileAsString(file, true) + "\r\n");
			out.write("\r\n");

			out.flush();
		}
	}
	
	
	public synchronized void start() {
		Log.log("Creating to ClientWorker/Thread, connected to: " + remoteIP + ".");
		running = true;
		thread = new Thread(this, "Client Worker/" + remoteIP);
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}

	public synchronized void stop() {
		Log.log("Stopping ClientWorker/Thread, connected to: " + remoteIP + ".");

		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
