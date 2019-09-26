package CourseClub.register;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course {

	private long id;
	private String title;
	private String teacher;
	private List<Student> students;

	public Course() {
		// stub
	}

	public void setId(long id2) {
		this.id = id2;

	}
}
