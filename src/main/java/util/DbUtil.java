package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class DbUtil {
	private static Logger log = Logger.getLogger(DbUtil.class);
	/**数据源对象，存放多个数据源*/
	public static Map<String, C3p0Plugin> dsMap = new HashMap<String, C3p0Plugin>();
	public static Map<String, ActiveRecordPlugin> arpMap = new HashMap<String, ActiveRecordPlugin>();
	/**
	 * 设置数据源
	 * @param dbName
	 */
	private synchronized static void setDateSource(String dbName){
		String jdbcUrl = String.format(PropUtil.getInstance().getValue("sql.jdbcUrl").trim(),dbName);
		String user = PropUtil.getInstance().getValue("sql.user").trim();
		String pass = PropUtil.getInstance().getValue("sql.password").trim();
		C3p0Plugin c3p0Plugin = new C3p0Plugin(jdbcUrl, user, pass);
		c3p0Plugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dbName,c3p0Plugin);
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));//设置大小写不敏感
		arp.start();
		
		dsMap.put(dbName, c3p0Plugin);
		arpMap.put(dbName, arp);
	}
	/**
	 * 获取数据源
	 * @param dbName
	 * @return
	 */
	public static C3p0Plugin getDataSource(String dbName){
		if(dbName==null){
			dbName = PropUtil.getInstance().getValue("sql.dbname").trim();
		}
		C3p0Plugin c = dsMap.get(dbName);		
		if(c==null){
			setDateSource(dbName);
			C3p0Plugin c1 = dsMap.get(dbName);
			return c1;
		}else{
			return c;
		}
	}
	/**
	 * 关闭所有数据源
	 */
	public synchronized void closeAllDateSource(){
		Set<String> keys = dsMap.keySet();
		for(String dbName:keys){
			C3p0Plugin c = dsMap.get(dbName);
			c.stop();
			dsMap.remove(dbName);
		}
		for(String dbName:keys){
			ActiveRecordPlugin c = arpMap.get(dbName);
			c.stop();
			arpMap.remove(dbName);
		}
	}
	/**
	 * 执行更新操作(非线程安全)
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public static boolean update(String dbName,String sql){
		if(dbName==null){
			dbName = PropUtil.getInstance().getValue("sql.dbname").trim();
		}
		log.debug("dbName:"+dbName+",sql:"+sql);
		getDataSource(dbName);
		return Db.use(""+dbName).update(sql)>0;
	}
	/**
	 * 查询集合
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public static List<Map<String,Object>> list1(String dbName,String sql){
		if(dbName==null){
			dbName = PropUtil.getInstance().getValue("sql.dbname").trim();
		}
		log.debug("dbName:"+dbName+",sql:"+sql);
		getDataSource(dbName);
		//Record r = Db.use(""+dbName).findFirst(sql);
		//String [] cname = r.getColumnNames();
		List<Record> list = Db.use(""+dbName).find(sql);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(Record r:list){
			String [] colums = r.getColumnNames();
			Map<String,Object> map = new HashMap<String,Object>();
			for(String col:colums){
				map.put(col, r.get(col));
			}
			result.add(map);
		}
		return result;
	}
	/**
	 * 获取单个信息
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public static Record info(String dbName,String sql){
		if(dbName==null){
			dbName = PropUtil.getInstance().getValue("sql.dbname").trim();
		}
		log.debug("dbName:"+dbName+",sql:"+sql);
		getDataSource(dbName);
		return Db.use(""+dbName).findFirst(sql);
	}
	/**
	 * 执行更新操作(非线程安全)
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public static boolean update(String dbName,String sql,Object... params){
		if(dbName==null){
			dbName = PropUtil.getInstance().getValue("sql.dbname").trim();
		}
		log.debug("dbName:"+dbName+",sql:"+sql);
		getDataSource(dbName);
		
		return Db.use(""+dbName).update(sql,params)>0;
	}
	/**
	 * 查询集合
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public static List<Record> list(String dbName,String sql,Object... params){
		if(dbName==null){
			dbName = PropUtil.getInstance().getValue("sql.dbname").trim();
		}
		log.debug("dbName:"+dbName+",sql:"+sql);
		getDataSource(dbName);
		return Db.use(""+dbName).find(sql,params);
	}
	/**
	 * 查询集合
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public static List<Record> list(String dbName,String sql){
		if(dbName==null){
			dbName = PropUtil.getInstance().getValue("sql.dbname").trim();
		}
		log.debug("dbName:"+dbName+",sql:"+sql);
		getDataSource(dbName);
		return Db.use(""+dbName).find(sql);
	}
	/**
	 * 获取单个信息
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public static Record info(String dbName,String sql,Object... params){
		if(dbName==null){
			dbName = PropUtil.getInstance().getValue("sql.dbname").trim();
		}
		log.debug("dbName:"+dbName+",sql:"+sql);
		getDataSource(dbName);
		return Db.use(""+dbName).findFirst(sql,params);
	}
	public static void main(String a[]){
		List<Record> list = DbUtil.list(null, "select * from test");
	}
	
}
