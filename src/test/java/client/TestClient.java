package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import net.sf.json.JSONObject;
import util.Constants;

public class TestClient extends Thread{
	Socket client;
	OutputStream out;//客户端发给服务器的
	InputStream input;//服务器发给客户端的
	
	PrintWriter pw;
	BufferedReader br;
	public TestClient(){
		try {
			client = new Socket("127.0.0.1", 1013);
			out = client.getOutputStream();//客户端发给服务器的
			input = client.getInputStream();//服务器发给客户端的
			pw = new PrintWriter(out);
			br = new BufferedReader(new InputStreamReader(input,"UTF-8"));
			JSONObject join = new JSONObject();
			join.put("a", Constants.AVTIVE_TYPE.CONNECT);
			join.put("un", "testuser");
			this.sendMsg(join.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		getInput();//持续获取用户输入
	}
	//{"a":"123","un":"123123123123123123"}
	//{"a":"1","un":"12345"}
	private class InputThread extends Thread{
		public void run(){
			try {
				InputStream sysin = System.in;
				BufferedReader sysbr = new BufferedReader(new InputStreamReader(sysin,"UTF-8"));
				String instring = sysbr.readLine();
				while (instring!=null) {
					sendMsg(instring);
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run(){
		System.out.println("开始运行");
		try {
			String rt = br.readLine();
			while(rt!=null){
				System.out.println("收到服务器消息："+rt);
				rt = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getInput(){
		new InputThread().start();
	}
	
	public void sendMsg(String data){
		pw.write(data+"\n");
		pw.flush();
	}
	
	public static void main(String [] a){
		new TestClient().start();
	}
}
