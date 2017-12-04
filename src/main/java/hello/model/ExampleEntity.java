package hello.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;
import java.util.UUID;

@Entity
public class ExampleEntity {

	@Id
	private String id;

	@Index
	private Date created;

	public ExampleEntity() {
		id = UUID.randomUUID().toString();
		created = new Date();
	}

	public String getId() {
		return id;
	}

	public Date getCreated() {
		return created;
	}

}
