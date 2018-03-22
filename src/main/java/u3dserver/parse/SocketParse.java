package u3dserver.parse;
/**
 * 调用socketclient的类必须实现这个接口。。。client会想parse发消息
 * 负责对接收到的请求进行翻译，不管是服务端，还是客户端都可以实现这个借口。
 * 在UI或业务处理端，实现这个接口的方法。然后在socket线程中调用这个区解析
 * @author zhangqing
 *
 */
public interface SocketParse {
	/**
	 * 翻译
	 * @param msg
	 */
	public abstract void parse(String msg);
}
