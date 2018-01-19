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

public class DbUtil_bak {
	private static Logger log = Logger.getLogger("DbUtil");
	/**数据源对象，存放多个数据源*/
	public static Map<Integer, C3p0Plugin> dsMap = new HashMap<Integer, C3p0Plugin>();
	public static Map<Integer, ActiveRecordPlugin> arpMap = new HashMap<Integer, ActiveRecordPlugin>();
	/**
	 * 设置数据源
	 * @param domainId
	 */
	private synchronized static void setDateSource(Integer domainId){
		String dbstart = PropUtil.getInstance().getValue("dbstart");
		String dbname = dbstart+domainId;
		String jdbcUrl = String.format(PropUtil.getInstance().getValue("jdbcUrl").trim(),dbname);
		String user = PropUtil.getInstance().getValue("user").trim();
		String pass = PropUtil.getInstance().getValue("password").trim();
		C3p0Plugin c3p0Plugin = new C3p0Plugin(jdbcUrl, user, pass);
		c3p0Plugin.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(""+domainId,c3p0Plugin);
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));//设置大小写不敏感
		arp.start();
		
		dsMap.put(domainId, c3p0Plugin);
		arpMap.put(domainId, arp);
	}
	/**
	 * 获取数据源
	 * @param domainId
	 * @return
	 */
	public static C3p0Plugin getDataSource(Integer domainId){
		C3p0Plugin c = dsMap.get(domainId);		
		if(c==null){
			setDateSource(domainId);
			C3p0Plugin c1 = dsMap.get(domainId);
			return c1;
		}else{
			return c;
		}
	}
	/**
	 * 关闭所有数据源
	 */
	public synchronized void closeAllDateSource(){
		Set<Integer> keys = dsMap.keySet();
		for(Integer domainId:keys){
			C3p0Plugin c = dsMap.get(domainId);
			c.stop();
			dsMap.remove(domainId);
		}
		for(Integer domainId:keys){
			ActiveRecordPlugin c = arpMap.get(domainId);
			c.stop();
			arpMap.remove(domainId);
		}
	}
	/**
	 * 执行更新操作(非线程安全)
	 * @param domainId
	 * @param sql
	 * @return
	 */
	public static boolean update(Integer domainId,String sql){
		log.debug("domainid:"+domainId+",sql:"+sql);
		getDataSource(domainId);
		return Db.use(""+domainId).update(sql)>0;
	}
	/**
	 * 查询集合
	 * @param domainId
	 * @param sql
	 * @return
	 */
	public static List<Map<String,Object>> list(Integer domainId,String sql){
		log.debug("domainid:"+domainId+",sql:"+sql);
		getDataSource(domainId);
		//Record r = Db.use(""+domainId).findFirst(sql);
		//String [] cname = r.getColumnNames();
		List<Record> list = Db.use(""+domainId).find(sql);
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
	 * @param domainId
	 * @param sql
	 * @return
	 */
	public static Record info(Integer domainId,String sql){
		log.debug("domainid:"+domainId+",sql:"+sql);
		getDataSource(domainId);
		return Db.use(""+domainId).findFirst(sql);
	}
	/**
	 * 执行更新操作(非线程安全)
	 * @param domainId
	 * @param sql
	 * @return
	 */
	public static boolean update(Integer domainId,String sql,Object... params){
		log.debug("domainid:"+domainId+",sql:"+sql);
		getDataSource(domainId);
		
		return Db.use(""+domainId).update(sql,params)>0;
	}
	/**
	 * 查询集合
	 * @param domainId
	 * @param sql
	 * @return
	 */
	public static List<Record> list(Integer domainId,String sql,Object... params){
		log.debug("domainid:"+domainId+",sql:"+sql);
		getDataSource(domainId);
		return Db.use(""+domainId).find(sql,params);
	}
	/**
	 * 获取单个信息
	 * @param domainId
	 * @param sql
	 * @return
	 */
	public static Record info(Integer domainId,String sql,Object... params){
		log.debug("domainid:"+domainId+",sql:"+sql);
		getDataSource(domainId);
		return Db.use(""+domainId).findFirst(sql,params);
	}
	
}
