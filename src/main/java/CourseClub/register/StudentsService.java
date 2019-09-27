package CourseClub.register;

import java.util.ArrayList;
import java.util.List;

public class StudentsService {

	private List<Student> students;
	private static long nextId = 0L;

	public StudentsService() {
		this.students = new ArrayList<Student>();
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public static long getNextId() {
		return nextId;
	}

	public static void setNextId(long nextId) {
		StudentsService.nextId = nextId;
	}

	public List<Student> getAllStudents(long courseId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Student getStudent(long courseId, long studentId) {
		// TODO Auto-generated method stub
		return null;
	}

}
