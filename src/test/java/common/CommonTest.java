package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.junit.Test;

import net.sf.json.JSONObject;
import util.HttpUtil;
import util.PropUtil;

public class CommonTest {
	
	@Test
	public void 机器信息() throws UnknownHostException, UnsupportedEncodingException{
		InetAddress inetaddres = InetAddress.getLocalHost();
		;
		System.out.println(inetaddres.getHostName());
		System.out.println(inetaddres.getHostAddress());
		System.out.println(inetaddres.getAllByName("CK_DEV01").length);
		for(InetAddress i:inetaddres.getAllByName("CK_DEV01")){
			System.out.println(i.getHostName()+"=="+i.getHostAddress());
		}
		
		InetAddress inetaddres1 = InetAddress.getByName("192.168.0.222");
		System.out.println(inetaddres1.getHostName());
		System.out.println(inetaddres1.getHostAddress());
		for(InetAddress i:inetaddres1.getAllByName(inetaddres1.getHostName())){
			System.out.println(i.getHostName()+"=="+i.getHostAddress());
		}
	}
	
	@Test
	public void 获取网络信息() throws IOException{
		URL xcz = new URL("http://www.x-cz.cn");
		URL url = new URL(xcz, "/#page2");
		System.out.println(url.getHost());
		System.out.println(url.getProtocol());
		System.out.println(url.getDefaultPort());
		System.out.println(url.getQuery());
		System.out.println(url.getRef());
		System.out.println(url.getUserInfo());
		System.out.println(url.getPath());
		InputStream input = url.openStream();
		InputStreamReader reader = new InputStreamReader(input,"UTF-8");
		BufferedReader br = new BufferedReader(reader);
		StringBuffer sb = new StringBuffer();
		String data = br.readLine();
		while (data!=null) {
			sb.append(data+"\n");
			data = br.readLine();
		}
		System.out.println(sb.toString());
	}
	
	@Test
	public void 用户登录(){
		JSONObject obj = HttpUtil.getSimpleHttpresult(PropUtil.getInstance().getValue("api.user.login"), "?account=zhangqing&password=202cb962ac59075b964b7152d234b70");
		System.out.println(obj);
	}
	
}
