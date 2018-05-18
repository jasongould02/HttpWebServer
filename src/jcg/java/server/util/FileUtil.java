package jcg.java.server.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
public class FileUtil {


	/*
	 * Should this return a File object if the actual file exists or 
	 * just act like creating a new File object regardless if the file 
	 * actually exists?
	 */
	public static File getFile(String name) {
		return new File(name);
	}
	
	public static String getFileExtension(File file) {
		if(file.exists()) {
			String[] split = file.getName().split("\\.");
			String fileExtension = split[split.length-1];
			return fileExtension;
		} else {
			return null;
		}
	}
	
	/**
	 * Return the entire content of the file as a single string.
	 * 
	 * @param file to be read
	 * @param newLine true if including '\n' at the end of each line 
	 * @returns a String
	 * @throws IOException
	 */
	public static String getFileAsString(File file, boolean newLine) throws IOException { 
		String fileText = "";
		if(file.exists()) {
			List<String> lines = Files.readAllLines(file.toPath());
			for(String s : lines) {
				fileText += (newLine)? (s+"\n") : (s); // Inline if statement how sick
				//fileText += s + "\n";
			}
			lines = null;
		}
		return fileText;
	}
	
	public static byte[] getFileContentAsByte(File file) throws IOException {
		if(file.exists()) {
			return Files.readAllBytes(file.toPath());
		} else {
			return null;
		}
	}

	//TODO: Inefficient since it has to re-read the entire file in order to get the file length
	public static int getFileContentLength(File file) throws IOException {
		return getFileAsString(file, false).length(); // file.exists() check is already within this function
	}
	
	
	
}