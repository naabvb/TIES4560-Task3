package CourseClub.register;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/courses")
public class CoursesResource {

	CoursesService coursesService = new CoursesService();
	StudentsResource studentsResource = new StudentsResource();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getCourses(@QueryParam("size") int size, @QueryParam("teacher") String teacher) {
		if (teacher != null) {
			return coursesService.getFilteredByTeacher(teacher, size);
		}
		if (size != 0) {
			return coursesService.getFilteredBySize(size);
		}

		return coursesService.getAllCourses();
	}

	public CoursesService getCoursesService() {
		return coursesService;
	}

	@GET
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course getCourse(@PathParam("courseId") long id) {
		Course course = coursesService.getCourse(id);
		return course;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Course addCourse(Course course) {
		return coursesService.addCourse(course);
	}

	@PUT
	@Path("/{courseId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Course updateCourse(@PathParam("courseId") long id, Course course) {
		course.setId(id);
		return coursesService.updateCourse(course);
	}

	@DELETE
	@Path("/{courseId}")
	public void deleteCourse(@PathParam("courseId") long id) {
		coursesService.removeCourse(id);
	}

	@Path("/{courseId}/students")
	public StudentsResource getStudentsResource() {
		return studentsResource;
	}

}
