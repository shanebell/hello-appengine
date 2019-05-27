package hello.config;

import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import hello.example.ExampleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = {"/*"})
public class ObjectifyWebFilter extends ObjectifyFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectifyWebFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		LOGGER.info("Registering Objectify entities");
		ObjectifyService.factory().register(ExampleEntity.class);
	}
}
