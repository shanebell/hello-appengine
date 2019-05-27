package hello.tasks;

import hello.example.ExampleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import static com.googlecode.objectify.ObjectifyService.ofy;

@RestController()
@RequestMapping("/task")
public class TaskController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

	public TaskController() {
	}

	@PostMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
	public void handleTask(
			@RequestHeader("X-AppEngine-QueueName") String queueName,
			@RequestHeader("X-AppEngine-TaskName") String taskName,
			@RequestHeader("X-AppEngine-TaskRetryCount") int retryCount
	) {
		LOGGER.info("Handling task - queueName: {}, taskName: {}, retryCount: {}", queueName, taskName, retryCount);

		if (retryCount < 3) {
			LOGGER.info("Simulating a failure");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		LOGGER.info("Saving an entity");
		ofy().save()
			.entity(new ExampleEntity())
			.now();
	}

}
