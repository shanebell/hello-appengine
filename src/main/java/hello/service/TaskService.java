package hello.service;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

	private final Queue defaultQueue;

	public TaskService(Queue defaultQueue) {
		this.defaultQueue = defaultQueue;
	}

	public TaskHandle queueTask() {
		TaskOptions taskOptions = TaskOptions.Builder
				.withUrl("/task/hello")
				.method(TaskOptions.Method.POST);
		TaskHandle taskHandle = defaultQueue.add(taskOptions);
		return taskHandle;
	}
}
