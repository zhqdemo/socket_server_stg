package u3dserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.jfinal.log.Logger;

import u3dserver.user.UserServer;
import util.LogUtil;

public class StartServer extends Thread{
	private String serverPath = "server";
	private ServerSocket server = null;
	public StartServer(ServerSocket server){
		this.server = server;
	}
	@Override
	public void run() {
		for(;;){
			Socket socket;
			try {
				LogUtil.info(serverPath, this, "等待接入");
				socket = server.accept();
				new UserServer(socket).start();
				MainServer.instance().online();
				LogUtil.info(serverPath, this, "新用户连接，在线用户数："+MainServer.instance().getOnline());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
