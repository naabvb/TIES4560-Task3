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

}
