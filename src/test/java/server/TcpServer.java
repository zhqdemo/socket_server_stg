package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
	public TcpServer() throws IOException{
		//创建socket服务
		ServerSocket server = new ServerSocket(21000);
		System.out.println("创建服务");
		//等待连接创建插座
		System.out.println("等待连接");
		Socket socket = server.accept();
		
		System.out.println("client connect");
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
				server.close();
				return;
			}else{
				pw.flush();
				data = reader.readLine();
			}
		}
	}
	
	public static void main(String [] orgs) throws IOException{
		new TcpServer();
	}
}
