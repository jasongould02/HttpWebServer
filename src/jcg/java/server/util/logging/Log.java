package jcg.java.server.util.logging;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import javax.xml.crypto.Data;

public class Log implements Runnable {

	private Thread thread;
	private boolean running = false;
	
	public static enum LogType {
		INFO, WARNING, ERROR, DUMP
	};
	
	//private static BufferedWriter w;
	private static PrintStream w; //= System.out;

	/*public static enum LogNameStyle {
		EXPANDED, SIMPLE
	};*/
	
	/*public static enum BufferType {
		UNBUFFERED, BUFFERED
	};*/

	static String filename = "debug.txt";
	static String debugtext = "";
	static boolean consoleLog = false; // If you want to log in console and log into file at same time
	//static LogNameStyle NAME_TYPE = LogNameStyle.EXPANDED;
	//static BufferType BUFFER_TYPE = BufferType.UNBUFFERED;

	
	/**
	 * This will toggle if the {@link Log#log(String)} calls will also tell the console to print
	 * 
	 * @param logToConsole
	 */
	public static void logToConsole(boolean logToConsole) {
		consoleLog = logToConsole;
	}
	
	/**
	 * This writes the given text and the writes a new line seperator then flushes the stream buffer
	 * 
	 * @param msg text to be logged
	 */
	public static synchronized void log(String msg) {
		System.out.println(msg); // TODO: REMOVE
		if(w != null) {
			/*w.write(msg);
			w.newLine();
			flushStream();*/
			w.println(msg);
			w.flush();
		}
	}
	
	
	/**
	 * Flushes the BufferedWriter
	 * @throws IOException
	 */
	private static synchronized void flushStream() throws IOException {
		if(w != null) {
			w.flush();
		}
	}
	
	protected static synchronized void closeStream() throws IOException {
		if(w != null) {
			w.flush();
			w.close();
		}
	}
	
	@Override
	public void run() {
		//w = new BufferedWriter(new FileWriter(new File(filename)));
		//w = new PrintWriter(new BufferedWriter(new FileWriter(new File(filename)))); // This is so ugly =/
		try {
			w = new PrintStream(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(running)
		{
			
		}
		// Flush before closing
		w.flush();
		w.close();
	}
	
	public synchronized void start() {
		Log.log("Starting logger");
		running = true;
		thread = new Thread(this, "Logger");
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}

	public synchronized void stop() {
		Log.log("Closing logger");
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

}
