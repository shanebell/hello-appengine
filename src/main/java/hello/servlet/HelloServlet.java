package hello.servlet;

import hello.model.ExampleEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@WebServlet(name = "HelloServlet", value = "/")
public class HelloServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// save a new entity
		ExampleEntity example = new ExampleEntity();
		ofy().save().entity(example).now();

		// list existing entities
		List<ExampleEntity> entities = ofy().load().type(ExampleEntity.class).order("-created").limit(20).list();
		int total = ofy().load().type(ExampleEntity.class).keys().list().size();

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!doctype html>");
		out.println("<html style=\"background: white;\">");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"https://staticresource.com/basic.css\" >");
		out.println("</head>");
		out.println("<body>");
		out.println("<h2>Hello App Engine</h2>");
		out.printf("<p>Running on Java version %s</p>\n", System.getProperty("java.version"));
		out.printf("<pre><p>There are %s stored entities</p></pre>", total);
		out.printf("<p><strong/>Details of the last 20 entities created:</strong></p>\n");
		out.println("<pre>");
		for (ExampleEntity entity : entities) {
			out.printf("%s %s\n", entity.getId(), entity.getCreated());
		}
		out.println("</pre>");
		out.println("</body>");
		out.println("</html>");
	}

}
