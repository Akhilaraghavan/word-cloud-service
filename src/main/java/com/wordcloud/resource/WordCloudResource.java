/**
 * 
 */
package com.wordcloud.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.wordcloud.dao.CassandraConnector;
import com.wordcloud.feed.HashTagParser;

/**
 * @author pc
 * 
 */
@Path("/wordcloud")
public class WordCloudResource {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void saveHashtag(Map<String, String> multivaluedMap) {
		CassandraConnector client = new CassandraConnector();
		client.connect();
		String time = multivaluedMap.get("time");
		Map<String, Integer> map = HashTagParser.parseText(multivaluedMap.get("text"));		
		for (String key : map.keySet()) {
			String query = "select word_count from wordcloud.WORD_CLOUD where event_time = "
					+ time + " and word = " + "\'" + key + "\'";
			ResultSet rs = (ResultSet) client.getSession().execute(query);
			int count = 0;
			while (rs != null && !rs.isExhausted()) {
				count = rs.one().getInt(0);
			}
			if (count != 0) {
				
				query = "delete from wordcloud.WORD_CLOUD where event_time = "
						+ time + " and word_key = "	+ "\'"	+ generateWordKey(count,key) + "\'";
				client.getSession().execute(query);
			}
			count += map.get(key);			
			query = "insert into wordcloud.WORD_CLOUD (word_count,word_key,word,event_time) values("
					+ count	+ ","+ "\'"+generateWordKey(count,key)+ "\'"+ ","+ "\'"+key+ "\'"+","+ time	+ ")";			
			client.getSession().execute(query);
			
		}
		client.close();
	}

	@GET
	@Path("/count")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Map<String, Integer> getTopWords(@QueryParam("limit")int limit, @QueryParam("time") long time) {
		CassandraConnector client = new CassandraConnector();
		client.connect();
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();

		String query = "select word,word_count from wordcloud.WORD_CLOUD where event_time = "
				+ time + " order by word_key desc limit " + limit;
		ResultSet rs = (ResultSet) client.getSession().execute(query);
		while (rs != null && !rs.isExhausted()) {
				Row row = rs.one();
				map.put(row.getString(0), row.getInt(1));				
		}
		client.close();
		return map;
	}


	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getTopWords() {
		return "hello world";
	}

	private static String generateWordKey(int count,String key){
		String cnt = String.valueOf(count);
		while (cnt.length() < 8) {
			cnt = "0" + cnt;
		}
		return  cnt+ "_"+ key;
	}

}
