package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * properties文件操作工具类
 * @author zhangqing
 * @date 2013-5-8 上午10:12:09
 */
public class PropUtil {
	/**stiac的自己的对象*/
	private static PropUtil prop;
	/**输入流*/
	private InputStream is;
	/**properties对象*/
    private OrderedProperties info;
    /**
     * 私有构造方法，实现单例模式
     */
    private PropUtil(){
    	initProp();
	}
    /**
     * 初始化properties文件构造方法
     * @author zhangqing
     * @date 2014-6-5 上午11:11:13
     */
    public void initProp(){
    	is = null;
    	info = null;
    	info = new OrderedProperties();
    	try {
    		/*is = new FileInputStream(this.getClass().getResource("/config.properties").getPath());*/
    		is = this.getClass().getResourceAsStream("/config.properties");
    		info.load(is);
    		is.close();
    	} catch (IOException e) {
    		System.out.println("读取配置文件错误");
    		e.printStackTrace();
    	}
    	//引入配置文件中的配置
    	List<String> importconfigList = this.getKeyStartWith("import.");
    	for(String importconfig:importconfigList){
    		String importconfigs [] = info.getProperty(importconfig).split(";");
    		for(int i = 0;i<importconfigs.length;i++){
    			InputStream is1 = this.getClass().getResourceAsStream("/"+importconfigs[i]);
    			OrderedProperties op = new OrderedProperties();
    			try {
    				op.load(is1);
    				is1.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    			Set<Object> keys = op.keySet();
    			Map<Object, Object> map = new HashMap<Object, Object>();
    			for(Object s:keys){
    				map.put(s, op.get(s));
    			}
    			info.putAll(map);
    		}
    	}
    	//end 引入配置文件中的配置
    }
    /**
     * 获得自身对象
     * @author zhangqing
     * @date 2013-5-8 上午10:21:05
     * @return
     */
    public static PropUtil getInstance(){
    	if(prop==null){
    		prop = new PropUtil();
    	}
    	return prop;
    }
    /**
     * 根据key获得对应的value
     * @author zhangqing
     * @date 2013-5-8 上午10:21:19
     * @param key 
     * @return
     * @throws UnsupportedEncodingException 
     */
    public String getValue(String key){
    	String result = null;
		try {
			result = new String(info.getProperty(key).getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return result;
    }
    /**
     * 根据key获得对应的value，没有value则返回默认值
     * @author zhangqing
     * @date 2013-5-8 上午10:21:44
     * @param key
     * @param defaultValue 默认值
     * @return
     * @throws UnsupportedEncodingException 
     */
    public String getValue(String key,String defaultValue){
    	String result = info.getProperty(key, defaultValue);
    	if(result!=null&&defaultValue.equals(result)){
    		return result;
    	}
    	try {
			result = new String(result.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
    }
    /**
     * 获得properties对象
     * @author zhangqing
     * @date 2013-5-8 上午10:22:10
     * @return
     */
    public OrderedProperties getProperties(){
    	return info;
    }
    
    /**
     * 已某开头的字符串获得配置的key集合
     * @author zhangqing
     * @date 2013-7-9 下午06:38:21
     * @param start
     * @return
     */
    public List<String> getKeyStartWith(String start){
    	Set<Object> set = info.keySet();
    	List<String> keys = new ArrayList<String>();
    	for(Object s:set){
    		if(s.toString().startsWith(start)){
    			keys.add(s.toString());
    		}
    	}
    	return keys;
    }
    /**
     * 保存properties文件
     * @author zhangqing
     * @date 2014-7-16 下午03:11:02
     * @param prop
     * @param filePath
     * @return
     */
    public boolean saveProp(OrderedProperties prop,String filePath){
    	try {
    		OutputStream out = new FileOutputStream(filePath);
			prop.store(out, "");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    public static OrderedProperties getPropByFile(String path){
    	/**输入流*/
    	InputStream inputs = null;
    	OrderedProperties prop = new OrderedProperties();
    	try {
    		inputs = new FileInputStream(path);
    		prop.load(inputs);
    		inputs.close();
    	} catch (IOException e) {
    		System.out.println("读取配置文件错误");
    		e.printStackTrace();
    		return prop;
    	}
    	return prop;
    }
    	
    
    public static void main(String [] arg) throws UnsupportedEncodingException{
    	String test = PropUtil.getInstance().getValue("download.iphone");
    	//test = new String(test.getBytes("ISO-8859-1"),"UTF-8");
    	System.out.println(test);
    	
    }

}
