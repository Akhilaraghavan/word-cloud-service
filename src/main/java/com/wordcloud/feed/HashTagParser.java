/**
 * 
 */
package com.wordcloud.feed;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.UrlValidator;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

/**
 * @author pc
 * 
 */
public class HashTagParser {

	private static final String HASHTAG_LETTERS = "\\p{L}\\p{M}";
	private static final String HASHTAG_NUMERALS = "\\p{Nd}";
	private static final String HASHTAG_SPECIAL_CHARS = "_" + // underscore
			"\\u200c" + // ZERO WIDTH NON-JOINER (ZWNJ)
			"\\u200d" + // ZERO WIDTH JOINER (ZWJ)
			"\\ua67e" + // CYRILLIC KAVYKA
			"\\u05be" + // HEBREW PUNCTUATION MAQAF
			"\\u05f3" + // HEBREW PUNCTUATION GERESH
			"\\u05f4" + // HEBREW PUNCTUATION GERSHAYIM
			"\\u309b" + // KATAKANA-HIRAGANA VOICED SOUND MARK
			"\\u309c" + // KATAKANA-HIRAGANA SEMI-VOICED SOUND MARK
			"\\u30a0" + // KATAKANA-HIRAGANA DOUBLE HYPHEN
			"\\u30fb" + // KATAKANA MIDDLE DOT
			"\\u3003" + // DITTO MARK
			"\\u0f0b" + // TIBETAN MARK INTERSYLLABIC TSHEG
			"\\u0f0c" + // TIBETAN MARK DELIMITER TSHEG BSTAR
			"\\u0f0d"; // TIBETAN MARK SHAD
	private static final String HASHTAG_LETTERS_NUMERALS = HASHTAG_LETTERS
			+ HASHTAG_NUMERALS + HASHTAG_SPECIAL_CHARS;
	private static final String HASHTAG_LETTERS_SET = "[" + HASHTAG_LETTERS
			+ "]";
	private static final String HASHTAG_LETTERS_NUMERALS_SET = "["
			+ HASHTAG_LETTERS_NUMERALS + "]";
	public static final Pattern VALID_HASHTAG = Pattern.compile("(#|\uFF03)("
			+ HASHTAG_LETTERS_NUMERALS_SET + "*" + HASHTAG_LETTERS_SET
			+ HASHTAG_LETTERS_NUMERALS_SET + "*)", Pattern.CASE_INSENSITIVE);
	
	public static Map<String,Integer> parseText(String text) {
		Map<String, Integer> map = new HashMap<>();

		try {
			UrlValidator urlValidator = new UrlValidator();
			if (urlValidator.isValid(text)) {
				URL feedUrl = new URL(text);
				SyndFeedInput input = new SyndFeedInput();
				SyndFeed feed = input.build(new XmlReader(feedUrl));

				for (SyndEntry entry : feed.getEntries()) {
					findMatcher(entry.getTitle(), map);
					findMatcher(entry.getDescription().getValue(), map);
				}

			} else {
				findMatcher(text, map);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR: " + ex.getMessage());
		}
		return map;

	}

	private static void findMatcher(String text, Map<String, Integer> map) {
		Matcher matcher = VALID_HASHTAG.matcher(text);
		while (matcher.find()) {
			String key = matcher.group();
			key = key.replaceAll("#", "");
			if (!map.containsKey(key)) {				
				map.put(key, 1);
			} else {
				int count = map.get(key) + 1;
				map.put(key, count);
			}

		}

	}
}
