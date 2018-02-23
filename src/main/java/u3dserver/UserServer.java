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
import u3dserver.dao.UserDao;
import util.Constants;
import util.HttpUtil;
import util.LogUtil;
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
	/**
	 * 解析接受的消息
	 * @param data
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
					this.parse(active, obj);//解析命令
				}catch(Exception e){
					LogUtil.log("json解析错误"+obj.toString());
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
	/**
	 * 解析命令
	 * @param active 动作
	 * @param obj 客户端发来的数据
	 */
	public void parse(int active,JSONObject obj){
		switch (active) {
			case Constants.AVTIVE_TYPE.LOGIN:{//用户登录
				this.login(obj);				
			}break;
			default:{//数据同步
				this.syncData(obj);;
			}break;
		}
	}
	/**
	 * 执行方法前验证
	 * @param obj
	 * @return
	 */
	public boolean before(JSONObject obj){
		if(this.userid==null){//如果用未登录，一切方法都不能执行
			JSONObject msg = new JSONObject();
			msg.put("a", Constants.AVTIVE_TYPE.ALERT);
			msg.put("c", Constants.RESULT_CODE.FAIL);
			msg.put("m", "用户为登录");
			this.sendMsg(msg.toString());
			return false;
		}
		return true;
	}
	/**
	 * 登录
	 * @param obj
	 */
	public void login(JSONObject obj){		
		String account = obj.getString("u");
		String password = obj.getString("p");
		JSONObject result = HttpUtil.getSimpleHttpresult(PropUtil.getInstance().getValue("api.user.login"), "?account="+account+"&password="+password);
		log.debug(result.toString());
		System.out.println(result);
		if(result.getInt("code")==Constants.RESULT_CODE.SUCCESS){//如果用户登录成功，加入在线用户列表
			this.userid = account;
			MainServer.instance().addUser(account, this);
		}
		this.sendMsg(result.toString());
	}
	/**
	 * 同步数据，将自己发出的指令同步到给其他用户
	 * @param obj
	 */
	public void syncData(JSONObject obj){
		if(!before(obj)) return;
		MainServer.instance().sendData2All(obj.toString());
	}
	/**
	 * 获取角色列表
	 * @param obj
	 */
	public void getRoleList(JSONObject obj){
		if(!before(obj)) return;
		
	}
	/**
	 * 获取角色信息
	 * @param obj
	 */
	public void getRoleInfo(JSONObject obj){
		if(!before(obj)) return;
		
	}
	/**
	 * 创建角色
	 * @param obj
	 */
	public void createRole(JSONObject obj){
		if(!before(obj)) return;
		
	}
	/**
	 * 删除角色
	 * @param obj
	 */
	public void deleteRole(JSONObject obj){
		if(!before(obj)) return;
		
	}
	/**
	 * 更新角色，包括角色名称，角色装备信息
	 * @param obj
	 */
	public void updateRole(JSONObject obj){
		if(!before(obj)) return;
		
	}
	/**
	 * 使用物品
	 * @param obj
	 */
	public void useItem(JSONObject obj){
		if(!before(obj)) return;
		
	}
	/**
	 * 加入游戏
	 * @param obj
	 */
	public void joinGame(JSONObject obj){
		if(!before(obj)) return;
		
	}
}
