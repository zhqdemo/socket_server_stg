package u3dserver.dao;

import net.sf.json.JSONObject;
import util.Constants;

/**
 * 用户数据库操作
 * @author zhangqing
 *
 */
public class UserDao {
	public static UserDao dao = new UserDao();
	/**
	 * 获取用户状态，如果数据库中已存在该用户
	 * @param account
	 * @param userinfo
	 * @return
	 */
	public int getUserState(String account,JSONObject userinfo){
		
		return Constants.CONS.USER_STATE_OK;
	}
	
	public JSONObject getRoleList(String account){
		return null;
	}
}
