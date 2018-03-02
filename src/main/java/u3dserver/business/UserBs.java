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
}
