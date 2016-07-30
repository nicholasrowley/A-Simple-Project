package serverSide;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Allows for multi-threaded server
 * @author Nick
 *
 */
public class ClientWorker extends Thread{
	private Socket client;

	public ClientWorker(Socket clientConnection) {
		client = clientConnection;
	}

	protected void finalize(){
		//Objects created in run method are finalized when
		//program terminates and thread exits
		try{
			client.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}

	public void run(){
		//while is connected
		try{
			DataInputStream in;
			while(true)
			{
				in = new DataInputStream(client.getInputStream());
				System.out.println(in.readUTF());
			}
		} catch (IOException e) {
			System.out.println();
			System.out.println("Connection lost with client server shutting down....");
			finalize();
			System.exit(-1);
		}
	}
}
