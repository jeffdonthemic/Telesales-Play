package service;

import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import java.net.InetSocketAddress;
import java.io.IOException;
import net.spy.memcached.MemcachedClient;

public class ConnectionManager {
	
	private static ConnectionManager ref;
	private static PartnerConnection connection;
	private static ConnectorConfig config;
	private static final String username = "jeff@jeffdouglas.com";
	private static final String password = "rTeU886@!FNHX1jAwrxfAd1CYHlFzV1sOh";
	private static final String SESSION_KEY = "sessionKey";
	private static final String SESSION_VALUE = "sessionValue";
	
	private ConnectionManager() { }

	public static ConnectionManager getConnectionManager() {
		if (ref == null)
			ref = new ConnectionManager();
		return ref;
	}
	
	// TODO - CACHE CONNECTION
	public PartnerConnection getConnection() {
			
		try { 
			
			config = new ConnectorConfig();
			config.setUsername(username);
			config.setPassword(password);
			connection = Connector.newConnection(config);

		} catch ( ConnectionException ce) {
			System.out.println("ConnectionException " +ce.getMessage());
		}        	

        //System.out.println("===========> SessionId...."+config.getSessionId());
        
		return connection;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(); 
	}

}