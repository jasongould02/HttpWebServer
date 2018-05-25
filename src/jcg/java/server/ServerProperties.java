package jcg.java.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerProperties {

	private static Properties sp = null;
	
	// Server Properties
	//private final String SITE_PATH; // The path to the folder which holds the site. No other files should be accessed by the user(s)
	//private final int PORT_NUMBER;
	//private final int BACK_LOG;
	//private final int MAX_CLIENTS; //Change to max threads? To be replaced by MAX_THREADS
	//private final int MAX_THREADS;
	
	public static String getProperty(String key) {
		if(ServerProperties.sp == null) {
			loadProperties();
		}
		return sp.getProperty(key);
	}
	
	private static void loadProperties(){
		try {
			if (sp == null) {
				sp = new Properties();
			}
			File f = new File("server.properties");
			if (!f.exists()) {
				return;
			} else {
				FileInputStream s = new FileInputStream(new File("server.properties"));
				sp.load(s);
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
