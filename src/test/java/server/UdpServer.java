package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpServer {
	public UdpServer() throws IOException{
		//
		DatagramSocket server = new DatagramSocket(22000);
		//用于接收数据
		byte [] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		//接收客户端数据
		server.receive(packet);
		String info = new String(data, 0, data.length);
		System.out.println("server receive dataa:"+info);
		
		//发送数据
		InetAddress client = packet.getAddress();
		int port = packet.getPort();
		byte [] senddata = "服务器收到了你的响应".getBytes();
		DatagramPacket sendPacket = new DatagramPacket(senddata, senddata.length,client,port);
		server.send(sendPacket);
		System.out.println("server send data:"+new String(senddata));
	}
	
	public static void main(String srg[]) throws IOException{
		new UdpServer();
	}
}
