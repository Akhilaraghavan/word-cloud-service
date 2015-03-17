/**
 * 
 */
package com.wordcloud.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import com.wordcloud.resource.WordCloudResource;

/**
 * @author pc
 *
 */
public class WordCloudTest {

	public static final String path = System.getProperty("user.home")+File.separator;
	
	@Test
	public void testSaveHashTags(){
		WordCloudResource rs = new WordCloudResource();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH");
		DateTime dt = DateTime.now();
		String time = String.valueOf(fmt.parseMillis(dt.toString(fmt)));
		Map<String, String> formData = new HashMap<String, String>();
		formData.put("time", String.valueOf(time));
		formData.put("text", "http://thomasyung.com/twitter_rss/twitter_json_to_rss.php?screen_name=thomasyung&list_name=mobiledev");
		rs.saveHashtag(formData);
		rs.saveHashtag(formData);
		rs.saveHashtag(formData);
		rs.saveHashtag(formData);
	}
	
	@Test
	public void testTopWords(){
		WordCloudResource rs = new WordCloudResource();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH");
		DateTime dt = DateTime.now();
		long time = fmt.parseMillis(dt.toString(fmt));
		Map<String,Integer> map = rs.getTopWords(53,time);
		assertTrue(map.size()>0);
	}
	
	
	
	

}
