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
                String[] parameters = br.readLine().split(";");
                String id = parameters[0];
                boolean use_sqr = parameters[1].toLowerCase().compareTo("true") == 0 ? true : false;
                boolean use_sqr_root = parameters[2].toLowerCase().compareTo("true") == 0 ? true : false;
                boolean use_sin = parameters[3].toLowerCase().compareTo("true") == 0 ? true : false;
                boolean use_cos = parameters[4].toLowerCase().compareTo("true") == 0 ? true : false;
                double constant_lower_bound = Double.parseDouble(parameters[5].toLowerCase());
                double constant_upper_bound = Double.parseDouble(parameters[6].toLowerCase());
                sock.close();

                Process p = new Process(path + id + ".csv", id, use_sqr, use_sqr_root, use_sin, use_cos, constant_lower_bound, constant_upper_bound);
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

