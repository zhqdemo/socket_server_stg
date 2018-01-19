package util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
/**
 * 重写properties类相关方法，实现获得的key值是有序的
 * @author zhangqing
 * @date 2013-5-14 上午10:27:08
 */
public class OrderedProperties extends Properties {
	 
    private static final long serialVersionUID = -4627607243846121965L;


    private final TreeSet<Object> keys = new TreeSet<Object>();

    
    public Enumeration<Object> keys() {
        return Collections.<Object> enumeration(keys);
    }
 
    public Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }
 
    public Set<Object> keySet() {
        return keys;
    }
 
    public Set<String> stringPropertyNames() {
        Set<String> set = new TreeSet<String>();
 
        for (Object key : this.keys) {
            set.add((String) key);
        }
 
        return set;
    }
}
