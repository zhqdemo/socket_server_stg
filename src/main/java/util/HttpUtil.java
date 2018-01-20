package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class HttpUtil {
	/**
	 * 功过传统URL获取请求结果，耗时优于getHttpresult方法
	 * @author zhangqing
	 * @date 2016年4月26日 上午11:39:06
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject getSimpleHttpresult(String url,String params){
//		String appSecret = PropUtil.getInstance().getValue("im.appsecret");
//		String appKey = PropUtil.getInstance().getValue("im.appkey");
//		String nonce = Calendar.getInstance().getTimeInMillis()+"";
//		String curTime = Calendar.getInstance().getTimeInMillis()/1000+"";
		try {
			URL url1 = new URL(url+(params==null?"":params));
			HttpURLConnection conn = (HttpURLConnection)url1.openConnection();
	        conn.setRequestProperty("contentType", "UTF-8");
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//	        conn.setRequestProperty("AppKey", appKey);
//	        conn.setRequestProperty("Nonce", nonce);
//	        conn.setRequestProperty("CurTime", curTime);
//	        conn.setRequestProperty("CheckSum", getCheckSum(appSecret, nonce, curTime));
	        
	        conn.setConnectTimeout(120 * 1000);
	        InputStream inStream =  conn.getInputStream();  //通过输入流获取html二进制数据
	        String htmlSource = readInputStream(inStream); 
	        inStream.close();
	        return JSONObject.fromObject(htmlSource);

		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		obj.put("code", -1);
		return obj;
	}
	/**
	 * 通过DefaultHttpClient 获取请求结果
	 * @author zhangqing
	 * @date 2016年4月26日 上午11:38:46
	 * @param url
	 * @param nvps
	 * @return
	 */
	public static JSONObject getHttpresult(String url,List<NameValuePair> nvps){
		DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
//
//        String appKey = PropUtil.getInstance().getValue("im.appkey");
//        String appSecret = PropUtil.getInstance().getValue("im.appsecret");
//        String nonce =  Calendar.getInstance().getTimeInMillis()+"";
//        String curTime = Calendar.getInstance().getTimeInMillis()/1000+"";
//        String checkSum = getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
//        httpPost.addHeader("AppKey", appKey);
//        httpPost.addHeader("Nonce", nonce);
//        httpPost.addHeader("CurTime", curTime);
//        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        try {
        	if(nvps!=null){        		
        		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        	}
			// 执行请求
			HttpResponse response;
			response = httpClient.execute(httpPost);
			// 打印执行结果
			return JSONObject.fromObject(EntityUtils.toString(response.getEntity(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        JSONObject obj = new JSONObject();
		obj.put("code", -1);
		return obj;
	}
	
	public static String readInputStream(InputStream instream) throws Exception {
		InputStreamReader reader = new InputStreamReader(instream,"UTF-8");
		BufferedReader br = new BufferedReader(reader);
		StringBuffer sb = new StringBuffer();
		String data = br.readLine();
		while (data!=null) {
			sb.append(data+"\n");
			data = br.readLine();
		} 
		return sb.toString();
    }
	 // 计算并获取CheckSum
	public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
	public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
    public static void main(String []a ){
    	System.out.println(HttpUtil.getSimpleHttpresult("http://www.baidu.com","").toString());
    }
}
