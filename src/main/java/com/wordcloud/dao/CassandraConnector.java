/**
 * 
 */
package com.wordcloud.dao;

import java.io.IOException;
import java.util.Properties;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * @author pc
 * 
 */
public final class CassandraConnector {

	private Cluster cluster;
	private Session session;
	private static final String host;
	private static final int port;

	static {
		Properties props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("db.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		host = String.valueOf(props.get("host"));
		port = Integer.parseInt(String.valueOf(props.get("port")));

	}

	public void connect() {
		this.cluster = Cluster.builder().addContactPoint(host).withPort(port)
				.build();
		session = cluster.connect();
	}

	/**
	 * @return the cluster
	 */
	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * @param cluster
	 *            the cluster to set
	 */
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	
	public void close(){
		session.close();
	}
	
	public void closeClient(){
		close();
		cluster.close();
	}
}
