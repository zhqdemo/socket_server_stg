package u3dserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.jfinal.log.Logger;

import u3dserver.user.UserServer;

public class StartServer extends Thread{
	private Logger log = Logger.getLogger(this.getClass()) ;
	private ServerSocket server = null;
	public StartServer(ServerSocket server){
		this.server = server;
	}
	@Override
	public void run() {
		for(;;){
			Socket socket;
			try {
				log.info("等待接入");
				socket = server.accept();
				new UserServer(socket).start();
				MainServer.instance().online();
				log.info("新用户连接，在线用户数："+MainServer.instance().getOnline());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
