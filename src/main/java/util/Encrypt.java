package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密解密类
 * @author zhangqing
 *
 */
public class Encrypt {
	/**
	 * MD5 加密函数
	 * @author zhangqing
	 * @date 2013-5-28 下午02:50:01
	 * @param str 需要加密的串
	 * @return
	 */
	public static String md5(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}
	static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
	public static String getMD5(InputStream file) {

		InputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis =  file;
			byte[] buffer = new byte[2048];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			byte[] b = md.digest();
			return byteToHexString(b);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	 /** 
     * @function 把byte[]数组转换成十六进制字符串表示形式 
     * @param tmp  要转换的byte[] 
     * @return 十六进制字符串表示形式 
     */  
    private static String byteToHexString(byte[] tmp) {  
        String s;  
        // 用字节表示就是 16 个字节  
        // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符  
        // 比如一个字节为01011011，用十六进制字符来表示就是“5b”  
        char str[] = new char[16 * 2];  
        int k = 0; // 表示转换结果中对应的字符位置  
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换  
            byte byte0 = tmp[i]; // 取第 i 个字节  
            str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移  
            str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换  
        }  
        s = new String(str); // 换后的结果转换为字符串  
        return s;  
    }  
	
	public static void main(String a []){
		System.out.println(Encrypt.md5("123"));
	}
	
}
