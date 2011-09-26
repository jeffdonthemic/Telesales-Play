package service;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

public class MyCache {
	private static final String NAMESPACE= "localhost:5d41402abc4b2a76b9719d91101";
	private static MyCache instance = null;
	private static MemcachedClient[] m = null;

	private MyCache() {
		try {
			m= new MemcachedClient[21];
			for (int i = 0; i <= 20; i ++) {
				MemcachedClient c =  new MemcachedClient(
                                                new BinaryConnectionFactory(),
						AddrUtil.getAddresses("localhost:9000"));
				m[i] = c;
			}
		} catch (Exception e) {

		}
	}

	public static synchronized MyCache getInstance() {
		System.out.println("Instance: " + instance);
		if(instance == null) {
			System.out.println("Creating a new instance");
			instance = new MyCache();
	     }
	     return instance;
	}

	public void set(String key, int ttl, final Object o) {
		getCache().set(NAMESPACE + key, ttl, o);
	}

	public Object get(String key) {
		Object o = getCache().get(NAMESPACE + key);
        if(o == null) {
        	System.out.println("Cache MISS for KEY: " + key);
        } else {
            System.out.println("Cache HIT for KEY: " + key);
        }
        return o;
	}

	public Object delete(String key) {
		return getCache().delete(NAMESPACE + key);
	}

	public MemcachedClient getCache() {
		MemcachedClient c= null;
		try {
			int i = (int) (Math.random()* 20);
			c = m[i];
		} catch(Exception e) {

		}
		return c;
	}
}