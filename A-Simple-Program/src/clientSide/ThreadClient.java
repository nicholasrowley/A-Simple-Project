package clientSide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Client side program.
 * @author Nick
 *
 */
public class ThreadClient extends Thread{
	private String serverIP;
	private int port;
	private Socket client;
	private String clientName;

	private JFrame frame;
	private JLabel serverOutput;
	private JTextField inputBox;
	private JButton submitButton;
	private boolean guiOpen;

	public ThreadClient( String name ) {
		clientName = name;
	}

	public synchronized void connectToServer(){
		serverIP = "192.168.1.18";
		port = 8068;
		try{
			System.out.println("Connecting to " + serverIP
					+ " on port " + port);
			client = new Socket(serverIP, port);
			System.out.println("Just connected to "
					+ client.getRemoteSocketAddress());

			/*
			inFromServer = client.getInputStream();
			in =
					new DataInputStream(inFromServer);
			System.out.println("Server says " + in.readUTF());*/
		} catch  (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void setUpGUI()
	{

		frame = new JFrame(clientName);

		//2. Optional: What happens when the frame closes?
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// close sockets, etc
				try {
					client.close();
					guiOpen = false;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		//3. Create components and put them in the frame.
		//...create emptyLabel...

		serverOutput = new JLabel("Send words to serverside: ");
		frame.getContentPane().add(serverOutput,BorderLayout.NORTH);
		serverOutput.setOpaque(true);
		serverOutput.setBackground(Color.white);


		//create text box
		inputBox = new JTextField("Input words here.");
		frame.getContentPane().add(inputBox, BorderLayout.CENTER);

		//create Button
		submitButton = new JButton("Submit");
		frame.getContentPane().add(submitButton, BorderLayout.SOUTH);

		submitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//when button is pushed
				try {
					String message = inputBox.getText();
					OutputStream outToServer = client.getOutputStream();
					DataOutputStream out = new DataOutputStream(outToServer);
					out.writeUTF(message);
					inputBox.setText(new String(""));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});

		//4. Size the frame.
		frame.pack();
		frame.setSize(300, 110);

		//5. Show it.
		frame.setVisible(true);

		guiOpen = true;
	}

	public void run(){
		connectToServer();
		setUpGUI();

		while(guiOpen == true ){
			//waits until window is closed.
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		Thread t1 = new ThreadClient("Client 1");
		Thread t2 = new ThreadClient("Client 2");
		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

}
