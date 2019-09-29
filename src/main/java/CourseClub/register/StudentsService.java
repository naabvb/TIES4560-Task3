package CourseClub.register;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
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

	public List<Student> getStudentsFromCourse(long courseId) {
		List<Student> filtered = new ArrayList<Student>();
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getOnCourseId() == courseId) {
				filtered.add(students.get(i));
			}
		}
		return filtered;
	}

	public Student getStudent(long courseId, long studentId) {
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getId() == studentId && students.get(i).getOnCourseId() == courseId)
				return students.get(i);
		}
		return null; // TODO EXCEPTIONS!!!
	}

	public Student addStudent(Student student, long courseId) {
		student.setId(nextId);
		student.setOnCourseId(courseId);
		nextId++;
		students.add(student);
		return student;
	}

	public Student updateStudent(Student student) {
		int index = findStudentIndex(student.getId());
		if (index >= 0) {
			students.set(index, student);
			return student;
		}
		return null; // TODO ex??
	}

	private int findStudentIndex(long id) {
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getId() == id)
				return i;
		}
		return -1; // TODO EXCEPTION
	}

	public void removeStudent(long id) {
		int index = findStudentIndex(id);
		if (index >= 0) {
			students.remove(index);
		}

		// TODO EX

	}

}
