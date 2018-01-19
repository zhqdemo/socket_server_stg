package u3dserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.LogUtil;


public class StartServer extends Thread{
	private ServerSocket server = null;
	public StartServer(ServerSocket server){
		this.server = server;
	}
	@Override
	public void run() {
		for(;;){
			Socket socket;
			try {
				LogUtil.log("等待连接");
				socket = server.accept();
				new UserServer(socket).start();
				MainServer.instance().online();
				System.out.println("新用户连接，在线用户数："+MainServer.instance().getOnline());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
