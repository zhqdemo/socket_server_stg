package u3dserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.log.Logger;

import util.PropUtil;

public class MainServer{
	private Logger log = Logger.getLogger(this.getClass());
	/**监听端口*/
	private int prot = Integer.parseInt(PropUtil.getInstance().getValue("server.port"));
	/**在线人数*/
	private int countOnlineUser = 0;
	/**自己实例*/
	private static MainServer mainServer=null;
	/**用户列表*/
	private List<String> listUser = new ArrayList<String>();
	/**用户列表*/
	private Map<String, UserServer> mapUser = new HashMap<String, UserServer>(); 
	/**GM用户列表*/
	private List<String> gmListUser = new ArrayList<String>();
	/**GM用户列表*/
	private Map<String, UserServer> gmMapUser = new HashMap<String, UserServer>(); 
	/**
	 * 私有构造方法
	 */
	private MainServer(){
		try {
			ServerSocket server = new ServerSocket(prot);			
			new StartServer(server).start();
			log.info("服务启动成功，监听端口："+prot);
		} catch (IOException e) {
			log.error("服务器启动失败："+e.getMessage());
			e.printStackTrace();
		}
		
	}
	/**
	 * 单例模式
	 * @return
	 */
	public static MainServer instance(){	
		if(mainServer==null){
			mainServer = new MainServer();
		}
		return mainServer;
	}
	/**
	 * main方法
	 * @param a
	 */
	public static void main(String [] a){
		MainServer.instance();
	}
	/**
	 * 设置在线人数
	 * @param online
	 */
	private synchronized void setOnline(int online){
		countOnlineUser = online;
	}
	/**
	 * 获取在线人数
	 * @return
	 */
	public int getOnline(){
		return countOnlineUser;
	}
	/**
	 * 上线
	 */
	public void online(){
		setOnline(getOnline()+1);
	}
	/**
	 * 下线
	 */
	public void unonline(){
		setOnline(getOnline()-1);
	}
	/***
	 * 给所所有用户发送消息
	 * @param data
	 */
	public void sendData2All(String data){
		log.info("发送消息给所有用户:"+listUser.size());
		for(String id:listUser){
			UserServer userServer = mapUser.get(id);
			userServer.sendMsg(data);
		}
	}
	/**
	 * 
	 * @param id
	 * @param user
	 */
	public void addUser(String id,UserServer user){
		log.info("加入新用户:"+id);
		UserServer u = mapUser.get(id);
		mapUser.put(id, user);
		if(u!=null){
			listUser.remove(id);
		}
		listUser.add(id);
	}
	/**
	 * 
	 * @param id
	 */
	public void deleteUser(String id){
		mapUser.remove(id);
		listUser.remove(id);
	}
}
