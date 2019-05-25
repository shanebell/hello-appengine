package hello.controller;

import hello.servlet.ObjectifyWebFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/task")
public class TaskController {

	private static final Log LOG = LogFactory.getLog(ObjectifyWebFilter.class);

	public TaskController() {
	}

	@PostMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
	public String handleTask(
			@RequestHeader("X-AppEngine-QueueName") String queueName,
			@RequestHeader("X-AppEngine-TaskName") String taskName,
			@RequestHeader("X-AppEngine-TaskRetryCount") String retryCount,
			@RequestHeader("X-AppEngine-TaskExecutionCount") String executionCount
	) {
		LOG.info(String.format("Handling task - queueName: %s, taskName: %s, executionCount: %s, retryCount: %s", queueName, taskName, executionCount, retryCount));
		return "ok";
	}

}
