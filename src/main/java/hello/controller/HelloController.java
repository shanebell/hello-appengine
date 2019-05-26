package hello.controller;

import com.google.appengine.api.taskqueue.TaskHandle;
import hello.model.ExampleEntity;
import hello.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

@RestController
public class HelloController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

	private final TaskService taskService;

	public HelloController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> hello(@RequestParam(value = "delay", required = false, defaultValue = "0") int delay) {
		LOGGER.info("Saying hello");

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

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("title", "Hello AppEngine");
		response.put("entityCount", total);
		response.put("entities", entities);
		return response;
	}

	@PostMapping(value = "/queue-task", produces = MediaType.APPLICATION_JSON_VALUE)
	public TaskHandle queueTask() {
		LOGGER.info("Queuing a single task");

		TaskHandle taskHandle = taskService.queueTask();
		return taskHandle;
	}

	@PostMapping(value = "/queue-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object queueTasks() {
		LOGGER.info("Queuing multiple tasks");

		List<TaskHandle> taskHandles = new ArrayList<>();
		for (int i=0; i<10; i++) {
			TaskHandle taskHandle = taskService.queueTask();
			taskHandles.add(taskHandle);
		}
		return taskHandles;
	}

}
