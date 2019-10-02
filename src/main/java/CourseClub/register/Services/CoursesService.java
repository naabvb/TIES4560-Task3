package CourseClub.register.Services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import CourseClub.register.Exceptions.BadRequestException;
import CourseClub.register.Exceptions.ResourceNotFoundException;
import CourseClub.register.Types.Course;
import CourseClub.register.Types.Link;

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
		if (index >= 0) {
			return courses.get(index);
		}
		return null;
	}

	private int findCourseIndex(long id) {
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).getId() == id)
				return i;
		}

		return -1;
	}

	public Course addCourse(Course course) {
		if (course.hasRequiredAttributes()) {
			course.setId(nextId);
			nextId++;
			courses.add(course);
			return course;
		} else {
			throw new BadRequestException("Couldn't add course; missing required attributes.");
		}
	}

	public Course updateCourse(Course course) {
		int index = findCourseIndex(course.getId());
		if (index >= 0) {
			if (course.hasRequiredAttributes()) {
				List<Link> links = courses.get(index).getLinks();
				course.setLinks(links);
				courses.set(index, course);
				return course;
			} else {
				throw new BadRequestException("Couldn't update course; missing required attributes.");
			}
		} else {
			throw new ResourceNotFoundException("Couldn't find course with id " + course.getId());
		}
	}

	public Response removeCourse(long id) {
		int index = findCourseIndex(id);
		if (index >= 0) {
			courses.remove(index);
			return Response.status(204).build();
		} else {
			throw new ResourceNotFoundException("Couldn't find course with id " + id);
		}
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
