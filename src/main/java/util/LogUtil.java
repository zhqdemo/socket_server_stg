package util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;

public class LogUtil {
	private Properties prop;
	private String logPath;
	//文件输出日志
	private String fileAppender;
	private RollingFileAppender appender;
	//文件输出日志级别
	private String level;
	/**日志对象对应的文件名，用来快速拿到日志对象，不需要重新组织配置，如果是一天一个日志，那第二天就要重新拿一个*/
	private Map<String,String> map;
	/**web项目路径，web日志输出路径，如果不配做，默认输出到tomcat的lob目录下，非web项目不需要配置*/
	private String projectPath = null;
	/**
	 * 私有构造方法不允许实例化
	 */
	private LogUtil(){
		map = new HashMap<String, String>();
		prop = new Properties();
		List<String> keys = PropUtil.getInstance().getKeyStartWith("log4j.");
		for(Object s:keys){
			prop.put(s, PropUtil.getInstance().getValue(s.toString()));
		}
		PropertyConfigurator.configure(prop);
		fileAppender = prop.getProperty("log4j.fileappender");
		level = prop.getProperty("log4j.fileappender.level");
		//prop.keys();
	}
	/**自身实例化对象*/
	private static LogUtil logins = new LogUtil();
	/**
	 * 获取日志
	 * @param path 路径
	 * @param clas 日志输出所在对象
	 * @return
	 */
	public static Logger getLog(String path,Object clas){
		String key = path;
		//没有指明路径的保存在defualt文件夹中
		if(key==null||key.equals("")){
			key="defual";
		}
		//Logger log=Logger.getLogger(th+"-"+key);
		Logger log = null;//=Logger.getLogger(key);
		//生成新的Appender.并将配置文件中输出到文件日志的不配拿到并生成新的日志配置，并将之前的删掉
		if(logins.appender==null){
			logins.appender =  (RollingFileAppender) Logger.getRootLogger().getAppender(logins.fileAppender);
			//Logger.getRootLogger().removeAppender(fileAppender);
		}
		logins.logPath = logins.prop.getProperty("log4j.fileappender.file");
		//将文件名进行格式化
		Calendar calendar = Calendar.getInstance();
		if(logins.logPath.contains("@y")){//以年输出
			String year = ""+calendar.get(Calendar.YEAR);
			logins.logPath = logins.logPath.replace("@y", year);
		}else if(logins.logPath.contains("@m")){//以月
			String year = ""+calendar.get(Calendar.YEAR);
			String month = ""+(calendar.get(Calendar.MONTH)+1);
			logins.logPath = logins.logPath.replace("@m", year+"-"+month);
		}else if(logins.logPath.contains("@w")){//以周
			String year = ""+calendar.get(Calendar.YEAR);
			String month = ""+(calendar.get(Calendar.MONTH)+1);
			String week = ""+calendar.get(Calendar.WEEK_OF_MONTH);
			logins.logPath = logins.logPath.replace("@w", year+"-"+month+"-"+week+"week");
		}else if(logins.logPath.contains("@d")){//以日
			String year = ""+calendar.get(Calendar.YEAR);
			String month = ""+(calendar.get(Calendar.MONTH)+1);
			String day = ""+calendar.get(Calendar.DAY_OF_MONTH);
			logins.logPath = logins.logPath.replace("@d", year+"-"+month+"-"+day);
		}else if(logins.logPath.contains("@h")){//以小时
			String year = ""+calendar.get(Calendar.YEAR);
			String month = ""+(calendar.get(Calendar.MONTH)+1);
			String day = ""+calendar.get(Calendar.DAY_OF_MONTH);
			String h = ""+calendar.get(Calendar.HOUR_OF_DAY);
			logins.logPath = logins.logPath.replace("@h", year+"-"+month+"-"+day+" "+h+"h");
		}
		String logPashts[] = logins.logPath.replace("\\", "/").split("/");
		String fileName = logPashts[logPashts.length-1].replace(".log", "");
		if(logins.map.get(key)!=null&&logins.map.get(key).equals(fileName)){
			return Logger.getLogger(key);			
		}
		//重置装配日志的配置
		log=Logger.getLogger(key);
		String path1 = logins.logPath.replace("@o", key);
		RollingFileAppender appender1 = new RollingFileAppender();
		appender1.setName(key+" "+fileName);
        appender1.setLayout(logins.appender.getLayout());
        appender1.setFile(path1);
        appender1.setAppend(logins.appender.getAppend());
        appender1.setBufferedIO(logins.appender.getBufferedIO());
        appender1.setBufferSize(logins.appender.getBufferSize());
        appender1.setMaxBackupIndex(logins.appender.getMaxBackupIndex());
        appender1.setMaximumFileSize(logins.appender.getMaximumFileSize());
        appender1.setEncoding(logins.appender.getEncoding());
        appender1.activateOptions();
        log.removeAllAppenders();
        log.addAppender(appender1);
        log.setLevel(Level.toLevel(logins.level));
        logins.map.put(key,fileName);
		return log;
	}
	/**
	 * @param path 路径
	 * @param clas 日志输出所在对象
	 * @param msg 日志信息
	 */
	public static void info(String path,Object clas,Object msg){
		getLog(path, clas).info(msg);
	}
	/**
	 * @param path 路径
	 * @param clas 日志输出所在对象
	 * @param msg 日志信息
	 */
	public static void debug(String path,Object clas,Object msg){
		getLog(path, clas).debug(msg);
	}
	/**
	 * @param path 路径
	 * @param clas 日志输出所在对象
	 * @param msg 日志信息
	 */
	public static void error(String path,Object clas,Object msg){
		getLog(path, clas).error(msg);
	}
	
}
