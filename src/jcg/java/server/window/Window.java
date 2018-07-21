package jcg.java.server.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import jcg.java.server.util.logging.Log;

public class Window {

	private JFrame frame;
	private boolean alive;
	
	private JButton shutdownServerButton;
	
	public Window(String frameName) {
		createWindow(frameName);
	}
	
	private void createWindow(String frameName) {
		shutdownServerButton = new JButton();
		shutdownServerButton.setText("Shutdown Server");
		shutdownServerButton.addActionListener(new ActionListener() {
			@Override
		public void actionPerformed(ActionEvent e) {
				alive = false;
				//System.out.println("SHUTTING DOWN THE SERVER");
				Log.log("FROM WINDOW. Shutting down server");
				//frame.dispose();
				System.exit(0);
			}
		});
		frame = new JFrame();
		frame.setSize(200, 200);
		frame.setTitle(frameName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(shutdownServerButton);
		frame.setVisible(true);
	}
	
	public boolean isServerAlive() {
		return alive;
	}
}
