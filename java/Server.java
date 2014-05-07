import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.InterruptedException;
import java.lang.Thread;

public class Server
{
	ServerSocket ss;
	String path = "/home/ubuntu/content_farm/igniipotent/angular-regress/app/java/";
	public Server () throws IOException
	{
		ss = new ServerSocket (9000);
	}
	
	public void Simple () throws IOException
	{
               while (1==1)
               {
		Socket sock = ss.accept();
                BufferedReader br = new BufferedReader (new InputStreamReader (sock.getInputStream()));
                String[] parameters = br.readLine().tokenize(";");
                String id = parameters[0];
                String use_sin = parameters[1];
                String use_cos = parameters[2];
                sock.close();
                Process p = new Process(path + id + ".csv", id);
                System.out.println(id);
                new Thread(p).start();
               }
	}

	public static void main (String[] args)
	{
		try
		{
			Server s = new Server();
			
			s.Simple();
		}
		catch (Exception e){e.printStackTrace();}
	}
}

