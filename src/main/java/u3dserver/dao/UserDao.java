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
	public int getUserState(String account){
		
		return Constants.CONS.USER_STATE_OK;
	}
	
	public JSONObject getRoleList(String account){
		return null;
	}
}