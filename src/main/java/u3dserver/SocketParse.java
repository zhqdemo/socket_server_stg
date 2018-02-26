package u3dserver;
/**
 * 调用调用socketclient的类必须实现这个接口。。。client会想parse发消息
 * @author zhangqing
 *
 */
public interface SocketParse {
	public abstract void parse(String msg);
}
