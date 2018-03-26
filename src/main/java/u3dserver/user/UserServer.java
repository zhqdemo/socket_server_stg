package u3dserver.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import u3dserver.MainServer;
import u3dserver.business.UserBs;
import u3dserver.dao.UserDao;
import util.Constants;
import util.DbUtil;
import util.HttpUtil;
import util.PropUtil;
/**
 * 用户socket服务线程
 * @author zhangqing
 *
 */
public class UserServer extends Thread{
	Logger log = Logger.getLogger(this.getClass());
	InputStream input;
	BufferedReader reader;
	OutputStream out;
	PrintWriter pw;
	private Socket socket = null;//socket
	private String userid = null;//用户ID
	private UserDao dao = UserDao.dao;//用户数据库操作类
	private UserParse parse = new UserParse(this);//操作解析类
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/***
	 * 构造方法初始化socket链接和输入输出参数
	 * @param socket
	 */
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
				log.info("服务器接收到请求："+data);
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
							log.info("用户下线："+this.userid);
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
	/**
	 * 解析接受的消息
	 * @param data 包括{a:动作指令,sid:sessionid每一个请求的唯一标示,返回消息时一并返回，用来跟客户端请求配对,...}
	 */
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
			log.error("收到数据非json"+data);
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
					//解析指令
					parse.parse(active, obj);
				}catch(Exception e){
					log.error("json解析错误"+obj.toString());
					//e.printStackTrace();
					return;
				}
			}
		}
		if(array!=null){
			//MainServer.instance().sendData2All(data);
		}		
		//向所有客户端发出
	}	
	
}
