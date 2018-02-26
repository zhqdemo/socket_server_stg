package u3dserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import util.LogUtil;
import util.PropUtil;

public class ManagerClientSocket extends Thread{
	private Socket client = null;
	private OutputStream out = null;
	private InputStream input = null;
	private SocketParse socketParse = null;
	private PrintWriter pw = null;
	private BufferedReader br = null;
	
	public ManagerClientSocket(SocketParse socketParse){
		this.socketParse = socketParse;
		try {
			client = new Socket(PropUtil.getInstance().getValue("server.id"), Integer.parseInt(PropUtil.getInstance().getValue("server.port")));
			out = client.getOutputStream();//客户端发给服务器的
			input = client.getInputStream();//服务器发给客户端的
			pw = new PrintWriter(out);
			br = new BufferedReader(new InputStreamReader(input,"UTF-8"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {	
		this.socketParse.parse("开始读取数据");
		try{			
			String line = br.readLine();
			while(line!=null){
				this.socketParse.parse(line);
				if(client.isClosed()){
					line=null;	
					br.close();
					pw.close();
					input.close();
					out.close();
					client.close();
				}else{						
					line = br.readLine();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}	
	public void sendMsg(String msg){
		pw.write(msg+"\n");
		pw.flush();
	}
	
}
