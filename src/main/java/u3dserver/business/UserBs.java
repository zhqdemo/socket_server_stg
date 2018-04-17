package u3dserver.business;

import java.util.Map;

import com.jfinal.plugin.activerecord.Record;

import net.sf.json.JSONObject;
import util.Constants;
import util.DbUtil;
import util.PropUtil;

public class UserBs {
	public static UserBs bs = new UserBs();
	/**
	 * 获取用户状态，如果数据库中已存在该用户
	 * @param account 账号
	 * @param userinfo 用户信息
	 * @return
	 */
	public Map<String,Object> getUserInfo(String account,JSONObject userinfo){
		String sql_userinfo = PropUtil.getInstance().getValue("sql.user.info");
		Record userInfo = DbUtil.info(null, sql_userinfo,account);
		if(userInfo==null){
			String sql_userInsert = PropUtil.getInstance().getValue("sql.user.insert");
			Object username = userinfo.get("username");
			Object img = userinfo.get("img");
			DbUtil.update(null, sql_userInsert,account,username,img);
			userInfo = DbUtil.info(null, sql_userinfo,account);
		}
		return DbUtil.Record2map(userInfo);
	}
	/**
	 * 用户登录
	 * @param account
	 * @param password
	 * @return
	 */
	@Deprecated
	public JSONObject login(String account,String password){
		JSONObject obj = new JSONObject();
		String sql_login = PropUtil.getInstance().getValue("sql.login.info");
		Record loginuser = DbUtil.info(null, sql_login,account);
		if(loginuser!=null){
			String p = loginuser.getStr("password");
			if(p!=null&&p.equals("password")){
				obj.put("code", 0);//账号不存在
				obj.put("data", loginuser);
			}else{
				obj.put("code", 6);//账号不存在
			}
		}else{
			obj.put("code", 8);//账号不存在
		}
		return obj;
	}
}
