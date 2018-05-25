package jcg.java.server.util.logging;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

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
	
	/*public static void log(LogType logtype, String... msgs) {
		String traceBackCaller_CLASS = new Exception().getStackTrace()[1].getClassName();
		String traceBackCaller_METHOD = new Exception().getStackTrace()[1].getMethodName();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a ");
		String formattedDate = sdf.format(date);
		for (String msg : msgs) {
			if (logtype == LogType.ERROR) {
				debugtext += "> [ERROR] :: [" + formattedDate + "] :: " + msg + "\n";
				debugtext += "\t> [ERROR] :: Error originated at <" + traceBackCaller_CLASS.toString() + "." + traceBackCaller_METHOD.toString() + "()> \n";
			} else if (logtype == LogType.WARNING) {
				debugtext += "> [WARNING] :: [" + formattedDate + "] :: " + msg + "\n";
				debugtext += "\t> [WARNING] :: Warning originated at <" + traceBackCaller_CLASS.toString() + "." +  traceBackCaller_METHOD.toString() + "()> \n";
			} else if(logtype == LogType.DUMP) {
				debugtext += "> [DUMP] :: [" + formattedDate + "] :: " + msg + "\n";
				debugtext += "\t> [DUMP] :: Dump originated at <" + traceBackCaller_CLASS.toString() + traceBackCaller_METHOD.toString() + "()> \n";
			} else {
				debugtext += "> [INFO] ::  [" + formattedDate + "] :: [" + traceBackCaller_CLASS.substring(traceBackCaller_CLASS.lastIndexOf('.')+1) + ".class] :: " + msg + "\n";
			}
		}
		if(BUFFER_TYPE == BufferType.UNBUFFERED) { 
			saveLog();
		} 
	}
	
	public static void clearLogBuffer() {
		debugtext = null;
	}
	
	public static void flushLogBuffer() {
		try { 
			FileWriter writer = new FileWriter("debug/" + filename);
			writer.write(debugtext);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void saveLog() {
		try {
			File file = new File("debug");
			file.mkdir();
			FileWriter writer = new FileWriter("debug/" + filename);
			writer.write(debugtext);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getLogPath() {
		File file = new File(filename);
		return file.getAbsolutePath();
	}

	public static File getLogFile() {
		File file = new File(filename);
		return file;
	}
	
	public static LogNameStyle getLogNameStyle() {
		return NAME_TYPE;
	}

	public static void setNameStyle(LogNameStyle type) {
		NAME_TYPE = type;
		if (type == LogNameStyle.EXPANDED) {
			filename = "DEBUG-" + expandedDate() + ".txt";
		} else if (type == LogNameStyle.SIMPLE) {
			filename = "DEBUG-" + simpleDate() + ".txt";
		}
	}
	
	public static void setBufferType(BufferType buffer) {
		BUFFER_TYPE = buffer;
	}
	
	public static BufferType getBufferType() {
		return BUFFER_TYPE;
	}

	private static String simpleDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM:dd:yyyy h:mm:ss a ");
		String formattedDate = sdf.format(date);
		formattedDate = formattedDate.replace("/", "-");
		formattedDate = formattedDate.replace(":", "-");
		formattedDate = formattedDate.substring(0, 10);
		return formattedDate;
	}

	private static String expandedDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM:dd:yyyy h:mm:ss a ");
		String formattedDate = sdf.format(date);
		formattedDate = formattedDate.replace("/", "-");
		formattedDate = formattedDate.replace(":", "-");
		return formattedDate;
	}

	public static void logStackTrace() {
		Log.log(LogType.DUMP, Thread.getAllStackTraces().toString());
		//Thread.getAllStackTraces().toString();
		Thread.dumpStack();
	}*/

	

}
