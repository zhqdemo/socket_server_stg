package util;
/***
 * 常量类
 * @author zhangqing
 *
 */
public class Constants {
	/**
	 * 动作类型
	 * @author zhangqing
	 *
	 */
	public static class AVTIVE_TYPE{
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
	}
}
