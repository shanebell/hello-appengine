package hello.controller;

import hello.model.ExampleEntity;
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
	public String handleTask(
			@RequestHeader(value = "X-AppEngine-QueueName", required = false) String queueName,
			@RequestHeader(value = "X-AppEngine-TaskName", required = false) String taskName,
			@RequestHeader(value = "X-AppEngine-TaskRetryCount", required = false) String retryCount,
			@RequestHeader(value = "X-AppEngine-TaskExecutionCount", required = false) String executionCount
	) {
		LOGGER.info("Handling task - queueName: {}, taskName: {}, executionCount: {}, retryCount: {}", queueName, taskName, executionCount, retryCount);

		// simulated failure
		if (System.currentTimeMillis() % 2 == 0) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// save a new entity
		ExampleEntity example = new ExampleEntity();
		ofy().save().entity(example).now();

		return "ok";
	}

}
