package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClient {
	public UdpClient() throws IOException{
		//发送消息，目标地址
		InetAddress address = InetAddress.getByName("127.0.0.1");
		DatagramSocket socket = new DatagramSocket();
		int port = 22000;
		byte[] data = "{user:\"张青\"}".getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length,address,port);
		socket.send(packet);
		System.out.println("client send data:"+new String(data));
		
		//接收收据
		byte[] getdata = new byte[1024];
		DatagramPacket getPacket = new DatagramPacket(getdata, getdata.length);
		socket.receive(getPacket);
		String getString = new String(getdata);
		System.out.println("client get data:"+getString);
	}
	public static void main(String[]srg) throws IOException{
		new UdpClient();
	}
}
