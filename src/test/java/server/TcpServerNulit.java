package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerNulit {
	private static int onlienUser = 0;
	public TcpServerNulit() throws IOException{
		ServerSocket server = new ServerSocket(1013);
		for(;;){
			System.out.println("等待用户接入");
			Socket socket = server.accept();
			new SigleServer(socket).start();
			System.out.println("新用户连接，在线用户数："+getOnline());
		}
	}
	
	public static void main(String [] srg) throws IOException{
		new TcpServerNulit();
	}
	private synchronized static void setOnline(int online){
		onlienUser = online;
	}
	private synchronized static int getOnline(){
		return onlienUser;
	}
	public static void online(){
		setOnline(getOnline()+1);
	}
	public static void unonline(){
		setOnline(getOnline()-1);
	}
	
}
class SigleServer extends Thread{
	private Socket socket = null;
	public SigleServer(Socket socket){
		this.socket = socket;
		TcpServerNulit.online();
	}
	
	public void run(){
		try{
			//输入流
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			//输出流
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(out);
			String data = reader.readLine();
			while (data!=null) {
				System.out.println("服务器接收到请求："+data);
				pw.write("server get msg:"+data+"\n");
				if(data.equals("exit")){
					pw.write("connect closed;\n");
					pw.flush();
					pw.close();
					out.close();
					reader.close();
					input.close();
					socket.close();
					TcpServerNulit.unonline();
					return;
				}else{
					pw.flush();
					data = reader.readLine();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			TcpServerNulit.unonline();
		}
	}
}
