package hello.service;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

	private final Queue defaultQueue;

	public TaskService(Queue defaultQueue) {
		this.defaultQueue = defaultQueue;
	}

	public TaskHandle queueTask() {
		LOGGER.info("Queueing a new task");
		TaskOptions taskOptions = TaskOptions.Builder
				.withUrl("/task/hello")
				.method(TaskOptions.Method.POST);
		return defaultQueue.add(taskOptions);
	}
}
