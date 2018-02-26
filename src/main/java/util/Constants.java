package util;
/***
 * 常量类
 * @author zhangqing *
 */
public class Constants {
	/**动作类型*/
	public static final class AVTIVE_TYPE{
		/**用户连接*/
		public static final int CONNECT = 1;
		/**重新连接*/
		public static final int RE_CONNECT = 2;
		/**丢失连接*/
		public static final int DES_CONNECT = 3;
		/**退出*/
		public static final int CLOSE_CONNECT = 4;
		/**创建对象*/
		public static final int CREATE_OBJ = 5;
		/**创建用户*/
		public static final int CREATE_USER = 6;
		/**摧毁对象*/
		public static final int DESTROY_OBJ = 7;
		/**请求同步数据*/
		public static final int SYNC_DATA = 8;
		/**登录*/
		public static final int LOGIN = 11;
		/**弹出消息*/
		public static final int ALERT = 12;
		/**角色列表*/
		public static final int ROLE_LIST = 10001;
	}
	/**错误代码*/
	public static final class RESULT_CODE{
		/**成功*/
		public static final int SUCCESS = 0;
		/**失败*/
		public static final int FAIL = 1;
	}
	/**常量*/
	public static final class CONS{
		/**用户状态正常*/
		public static final int USER_STATE_OK = 0;
		/**用户状态异常*/
		public static final int USER_STATE_ERROR = 1;
	}	
}
