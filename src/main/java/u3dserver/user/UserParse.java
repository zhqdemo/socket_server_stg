package u3dserver.user;

import java.util.List;
import java.util.Map;

import com.jfinal.log.Logger;

import net.sf.json.JSONObject;
import u3dserver.MainServer;
import u3dserver.business.UserBs;
import util.Constants;
import util.DbUtil;
import util.HttpUtil;
import util.PropUtil;

public class UserParse {
	Logger log = Logger.getLogger(this.getClass());
	private UserServer server = null;
	public UserParse(UserServer server){
		this.server = server;
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
			case Constants.AVTIVE_TYPE.ROLE_LIST:{//角色列表
				this.getRoleList(obj);				
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
		if(server.getUserid()==null){//如果用未登录，一切方法都不能执行
			JSONObject msg = new JSONObject();
			msg.put("a", Constants.AVTIVE_TYPE.ALERT);
			msg.put("c", Constants.RESULT_CODE.FAIL);
			msg.put("m", "用户未登录");
			server.sendMsg(msg.toString());
			return false;
		}
		return true;
	}
	/**
	 * 填充sid
	 * @param response
	 * @param request
	 * @return
	 */
	public JSONObject initSessionId(JSONObject response,JSONObject request){
		if(response == null){
			response = new JSONObject();
		}
		response.put("sid", request.get("sid"));
		response.put("a", request.get("a"));
		return response;
	}
	/**
	 * 登录
	 * @param obj
	 */
	public void login(JSONObject obj){		
		String account = obj.getString("u");
		String password = obj.getString("p");
		//账号认证暂时写在本地用数据查，实际应该通过接口统一管理
		//JSONObject result = HttpUtil.getSimpleHttpresult(PropUtil.getInstance().getValue("api.user.login"), "?account="+account+"&password="+password);
		JSONObject result = UserBs.bs.login(account, password);
		log.info("登录信息："+result.toString());
		JSONObject res = new JSONObject();
		res = this.initSessionId(res, obj);
		if(result.getInt("code")==Constants.RESULT_CODE.SUCCESS||true){//如果用户登录成功，加入在线用户列表
			server.setUserid(account);
			Map<String,Object> usermap = UserBs.bs.getUserInfo(account, result);
			if(usermap!=null){
				res.put("d", JSONObject.fromObject(usermap));
			}
			MainServer.instance().addUser(account, server);
		}
		log.info("服务器返回"+res.toString());
		server.sendMsg(res.toString());
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
		String sql = PropUtil.getInstance().getValue("sql.role.list");
		List<Map<String,Object>> list = DbUtil.list(null, sql,server.getUserid());
		JSONObject data = this.initSessionId(null, obj);
		data.put("d", list);
		server.sendMsg(data.toString());
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
		String rolename = obj.getString("name");
		String img = obj.getString("img");
		String sql = PropUtil.getInstance().getValue("sql.role.insert");
		boolean result = DbUtil.update(null, sql,server.getUserid(),rolename,img);
		JSONObject data = this.initSessionId(null, obj);
		data.put("c", result?Constants.RESULT_CODE.SUCCESS:Constants.RESULT_CODE.FAIL);
		server.sendMsg(data.toString());
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
	/**
	 * 获取角色装备
	 * @param obj
	 */
	public void getRoleEquip(JSONObject obj){
		if(!before(obj)) return;
		String sql = null;
	}
	/**
	 * 获取用户背包
	 * @param obj
	 */
	public void getUserPackage(JSONObject obj){
		if(!before(obj)) return;
		
	}
}
