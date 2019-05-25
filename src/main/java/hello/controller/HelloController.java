package hello.controller;

import hello.model.ExampleEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@RestController
public class HelloController {

	@GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
	public String hello(HttpServletRequest request) {

		// save a new entity
		ExampleEntity example = new ExampleEntity();
		ofy().save().entity(example).now();

		int delay = 0;
		String queryString = request.getQueryString();
		if(queryString != null && queryString.length() > 0) {
			delay = Integer.parseInt(queryString.substring(2));
		}

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// list existing entities
		List<ExampleEntity> entities = ofy().load().type(ExampleEntity.class).order("-created").limit(20).list();
		int total = ofy().load().type(ExampleEntity.class).keys().list().size();

		StringBuilder builder = new StringBuilder("Hello AppEngine\n\n");
		builder.append(String.format("Running on Java version %s\n\n", System.getProperty("java.version")));
		builder.append(String.format("There are %s stored entities\n\n", total));
		builder.append("Details of the last 20 entities created:\n\n");
		for (ExampleEntity entity : entities) {
			builder.append(String.format("%s %s\n", entity.getId(), entity.getCreated()));
		}

		return builder.toString();
	}

}
