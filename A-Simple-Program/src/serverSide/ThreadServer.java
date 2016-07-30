package serverSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Server side application.
 * @author Nick
 *
 */
public class ThreadServer extends Thread{
	private ServerSocket serverSocket;
	Socket server;

	public ThreadServer( int port ) throws IOException{
		serverSocket = new ServerSocket(port);
	}

	protected void finalize(){
		//Objects created in run method are finalized when
		//program terminates and thread exits
		try{
			server.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}

	public void run()
	{
		while(true)
		{
			try
			{
				ClientWorker w = new ClientWorker(serverSocket.accept());
				Thread t = new Thread(w);
				t.start();

			}catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				break;
			}
			catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
		int port = 8068;
		try
		{
			Thread t = new ThreadServer(port);
			t.start();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
