package CourseClub.register;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Student {
	private String name;
	private long id;
	private List<Long> courseIds = new ArrayList<Long>(); // Kurssit joilla ollaan

	public String getName() {
		return name;
	}

	public List<Long> getCourseIds() {
		return courseIds;
	}

	public void addCourseId(long courseId) {
		this.courseIds.add(courseId);
	}

	public void setCourseIds(List<Long> courseIds) {
		this.courseIds = courseIds;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Student() {
		// Stub
	}
}
