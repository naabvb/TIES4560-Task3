package CourseClub.register;

import java.util.ArrayList;
import java.util.List;

public class CoursesService {

	private List<Course> courses;
	private static long nextId = 0L;

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public CoursesService() {
		this.courses = new ArrayList<Course>();
	}

	public List<Course> getAllCourses() {
		return courses;
	}

	public Course getCourse(long id) {
		int index = findCourseIndex(id);
		return courses.get(index);
	}

	private int findCourseIndex(long id) {
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).getId() == id)
				return i;
		}

		return -1; // TODO EXCEPTION
	}

	public Course addCourse(Course course) {
		course.setId(nextId);
		// List<Student> empty = new ArrayList<Student>();
		// if (course.getStudents() == null)
		// course.setStudents(empty);
		nextId++;
		courses.add(course);
		return course;
	}

	public Course updateCourse(Course course) {
		int index = findCourseIndex(course.getId());
		if (index >= 0) {
			courses.set(index, course);
			return course;
		}
		return null; // TODO ex??
	}

	public void removeCourse(long id) {
		int index = findCourseIndex(id);
		if (index >= 0) {
			courses.remove(index);
		}

		// TODO EX

	}

	public List<Course> getFilteredByTeacher(String teacher, int size) {
		List<Course> filtered = new ArrayList<Course>();
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).getTeacher().equals(teacher))
				filtered.add(getCourse(i));
		}
		if (size != 0) {
			ArrayList<Course> filteredBySize = new ArrayList<Course>(filtered.subList(0, size));
			return filteredBySize;
		}
		return filtered;
	}

	public List<Course> getFilteredBySize(int size) {
		ArrayList<Course> filteredBySize = new ArrayList<Course>(courses.subList(0, size));
		return filteredBySize;
	}

}
