package hello.controller;

import com.google.appengine.api.taskqueue.TaskHandle;
import hello.model.ExampleEntity;
import hello.service.TaskService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

@RestController
public class HelloController {

	private final TaskService taskService;

	public HelloController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
	public String hello(@RequestParam(value = "delay", required = false, defaultValue = "0") int delay) {

		// save a new entity
		ExampleEntity example = new ExampleEntity();
		ofy().save().entity(example).now();

		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// ignore
			}
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

	@GetMapping(value = "/queue-task", produces = MediaType.APPLICATION_JSON_VALUE)
	public TaskHandle queueTask() {
		TaskHandle taskHandle = taskService.queueTask();
		return taskHandle;
	}

	@GetMapping(value = "/queue-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object queueTasks() {
		List<TaskHandle> taskHandles = new ArrayList<>();
		for (int i=0; i<10; i++) {
			TaskHandle taskHandle = taskService.queueTask();
			taskHandles.add(taskHandle);
		}
		return taskHandles;
	}

}
