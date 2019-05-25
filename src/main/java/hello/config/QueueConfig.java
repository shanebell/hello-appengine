package hello.config;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean(name = "defaultQueue")
	Queue defaultQueue() {
        return QueueFactory.getDefaultQueue();
    }

}
