package jcg.java.server.http.response;

import java.net.Socket;

public class Response implements Runnable {

	
	
	@Override
	public void run() {
		
	}

	private void createResponse(Socket clientSocket, String requestHeader) {
	
		
		
	}

	
	/*private void response(PrintWriter out, String line) throws IOException {
		String[] sep = line.split(" ");
		File file;
		
		file = getFileFromHeader(line);
		
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
		
		
		String fileExtension = FileUtil.getFileExtension(file);
		//System.out.println("Sending file: " + file.getName() + " With Extension: " + fileExtension); //TODO: Replace with log statement
		
		if(!file.exists() || FileUtil.getFileExtension(file) == null) {
			// TODO: HTTP 404 File Not Found
			out.write("HTTP/1.1 404 OK" + "\r\n");
			out.write("Connection: close" + "\r\n");
			//out.write("Content-Length: " + 0 + "\r\n");
			out.write("\r\n");
		} else {
			// TODO: check if file is allowed to be sent/accessed
			out.write("HTTP/1.1 200 OK" + "\r\n");
			out.write("Content-Type: text/" + fileExtension + "\r\n");
			out.write("Connection: keep-alive" + "\r\n");
			out.write("Content-Length: " + FileUtil.getFileContentLength(file) + "\r\n");
			out.write("\r\n");
			
			out.write(FileUtil.getFileAsString(file, true) + "\r\n");
			out.write("\r\n");
			
			out.flush();
		}
	}*/
	
}
