package u3dserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.Constants;
import util.HttpUtil;
import util.LogUtil;
import util.PropUtil;

public class UserServer extends Thread{
	Logger log = Logger.getLogger(this.getClass());
	InputStream input;
	BufferedReader reader;
	OutputStream out;
	PrintWriter pw;
	private Socket socket = null;
	private String userid = null;
	public UserServer(Socket socket){
		log.info("init");
		try{
			this.socket = socket;			
			input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			//输出流
			out = socket.getOutputStream();
			pw = new PrintWriter(out);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try{
			//输入流
			String data = reader.readLine();
			while (data!=null) {
				LogUtil.log("服务器接收到请求："+data);
				//将收到的数据发送给客户端
				if(data.equals("exit")){
					pw.write("connect closed;\n");
					pw.flush();
					pw.close();
					out.close();
					reader.close();
					input.close();
					socket.close();
					MainServer.instance().unonline();
					return;
				}else{					
					this.readData(data);
					if(socket.isClosed()){
						data=null;
						MainServer.instance().unonline();
						if(this.userid!=null){							
							MainServer.instance().deleteUser(this.userid);
							LogUtil.log("用户下线："+this.userid);
						}
					}else{						
						data = reader.readLine();
					}
				}
			}
		}
		catch (SocketException e) {
			e.printStackTrace();			
			MainServer.instance().unonline();
			MainServer.instance().deleteUser(this.userid);			
		}catch(Exception e){
			e.printStackTrace();
			MainServer.instance().unonline();
			MainServer.instance().deleteUser(this.userid);		
		}
		
	}
	/**
	 * 发送消息
	 * @param data
	 */
	public void sendMsg(String data){
		pw.write(data+"\n");
		pw.flush();
	}
	
	private void readData(String data){
		//sendMsg(data);
		JSONObject obj = null;
		JSONArray array = null;
		try{
			if(data.startsWith("{")){
				obj = JSONObject.fromObject(data);
			}else if(data.startsWith("[")){
				array = JSONArray.fromObject(data);
			}
		}catch(Exception e){
			LogUtil.log("收到数据非json"+data);
			//e.printStackTrace();
			return;
		}
		if(obj!=null){
			Object a = obj.get("a");
			if(a!=null){
				try{
					//{"a":"123","un":"123123123123123123"}
					//{"a":"1","un":"1234"}
					int active = Integer.parseInt(a.toString());
					switch (active) {
						case Constants.AVTIVE_TYPE.CONNECT:{
							String username = obj.getString("un");
							MainServer.instance().addUser(username, this);
							this.userid = username;
						}break;
						case Constants.AVTIVE_TYPE.LOGIN:{
							String account = obj.getString("u");
							String password = obj.getString("p");
							JSONObject result = HttpUtil.getSimpleHttpresult(PropUtil.getInstance().getValue("api.user.login"), "?account="+account+"&password="+password);
							log.debug(result.toString());
							System.out.println(result);
							this.sendMsg(result.toString());
						}break;
						default:{
							MainServer.instance().sendData2All(data);
						}break;
					}
				}catch(Exception e){
					LogUtil.log("json解析错误"+obj.toString());
					//e.printStackTrace();
					return;
				}
			}
		}
		if(array!=null){
			MainServer.instance().sendData2All(data);
		}
		
		//向所有客户端发出
	}
}
