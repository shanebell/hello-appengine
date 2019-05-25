package hello.servlet;

import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import hello.model.ExampleEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = {"/*"})
public class ObjectifyWebFilter extends ObjectifyFilter {

	private static final Log LOG = LogFactory.getLog(ObjectifyWebFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		LOG.info("Registering Objectify entities");
		ObjectifyService.factory().register(ExampleEntity.class);
	}
}
