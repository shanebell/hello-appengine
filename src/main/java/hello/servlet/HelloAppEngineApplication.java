package hello.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("hello")
public class HelloAppEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloAppEngineApplication.class, args);
	}
}