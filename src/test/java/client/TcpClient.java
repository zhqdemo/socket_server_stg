package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {
	public TcpClient() throws UnknownHostException, IOException{
		System.out.println("客户端："+Math.random());
		Socket client = new Socket("127.0.0.1", 1013);
		OutputStream out = client.getOutputStream();//客户端发给服务器的
		InputStream input = client.getInputStream();//服务器发给客户端的
		
		PrintWriter pw = new PrintWriter(out);
		BufferedReader br = new BufferedReader(new InputStreamReader(input,"UTF-8"));
		InputStream sysin = System.in;
		BufferedReader sysbr = new BufferedReader(new InputStreamReader(sysin,"UTF-8"));
		String instring = sysbr.readLine();
		while (instring!=null) {
			//System.out.println(instring);
			pw.write(instring+"\n");
			pw.flush();
			String rt = br.readLine();
			if(rt!=null){
				System.out.println(rt);
			}
			if(instring.equals("exit")){
				sysbr.close();
				sysin.close();
				br.close();
				pw.close();
				input.close();
				out.close();
				client.close();
				System.out.println("client closed");
				return;
			}else{
				instring = sysbr.readLine();
			}
		}
	}
	
	public static void main(String[]arg) throws UnknownHostException, IOException{
		new TcpClient();
	}
}
