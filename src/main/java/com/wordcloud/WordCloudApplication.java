/**
 * 
 */
package com.wordcloud;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.wordcloud.resource.WordCloudResource;

/**
 * @author pc
 * 
 */
public class WordCloudApplication extends Application<WordCloudConfiguration> {
	public static void main(String args[]) throws Exception {
		new WordCloudApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<WordCloudConfiguration> configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(WordCloudConfiguration configuration,
			Environment environment) throws Exception {
		Dynamic filter = environment.servlets().addFilter("CORS",
				CrossOriginFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
				true, "/*");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM,
				"GET,PUT,POST,DELETE,OPTIONS");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filter.setInitParameter(
				CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		filter.setInitParameter("allowedHeaders",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		filter.setInitParameter("allowCredentials", "true");
		final WordCloudResource resource = new WordCloudResource();
		environment.jersey().register(resource);
	}
}
