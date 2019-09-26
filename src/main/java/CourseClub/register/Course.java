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

	public void setId(long id) {
		this.id = id;

	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public String getTitle() {
		return title;
	}

	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}
}
