package CourseClub.register;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import CourseClub.register.Exceptions.ResourceNotFoundException;

@Singleton
@Path("/courses")
@PermitAll
public class CoursesResource {

	CoursesService coursesService = new CoursesService();

	StudentsResource studentsResource = new StudentsResource();
	FeedbackResource feedbackResource = new FeedbackResource();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getCourses(@QueryParam("size") int size, @QueryParam("teacher") String teacher)
			throws NoContentException {
		List<Course> courses = new ArrayList<>();
		if (teacher != null) {
			courses = coursesService.getFilteredByTeacher(teacher, size);
			if (!courses.isEmpty()) {
				return courses;
			} else {
				throw new NoContentException("No courses found.");
			}
		}
		if (size != 0) {
			courses = coursesService.getFilteredBySize(size);
			if (!courses.isEmpty()) {
				return courses;
			} else {
				throw new NoContentException("No courses found.");
			}
		}
		courses = coursesService.getAllCourses();
		if (!courses.isEmpty()) {
			return courses;
		} else {
			throw new NoContentException("No courses found.");
		}
	}

	@GET
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("admin")
	public Course getCourse(@PathParam("courseId") long id) {
		Course course = coursesService.getCourse(id);
		if (course == null) {
			throw new ResourceNotFoundException("Course with id " + id + " not found.");
		}
		return course;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCourse(Course course, @Context UriInfo uriInfo) {
		Course newCourse = coursesService.addCourse(course);
		String uri = uriInfo.getBaseUriBuilder().path(CoursesResource.class).path(Long.toString(course.getId())).build()
				.toString();
		newCourse.addLink(uri, "self");

		uri = uriInfo.getBaseUriBuilder().path(CoursesResource.class).path(CoursesResource.class, "getStudentsResource")
				.resolveTemplate("courseId", course.getId()).build().toString();
		newCourse.addLink(uri, "students");

		uri = uriInfo.getBaseUriBuilder().path(CoursesResource.class).path(CoursesResource.class, "getFeedbackResource")
				.resolveTemplate("courseId", course.getId()).build().toString();
		newCourse.addLink(uri, "feedback");

		String newId = String.valueOf(newCourse.getId());
		URI url = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(url).entity(newCourse).build();

	}

	@PUT
	@Path("/{courseId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Course updateCourse(@PathParam("courseId") long id, Course course) {
		course.setId(id);
		return coursesService.updateCourse(course);
	}

	@DELETE
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCourse(@PathParam("courseId") long id) {
		return coursesService.removeCourse(id);
	}

	@Path("/{courseId}/students")
	public StudentsResource getStudentsResource() {
		return studentsResource;
	}

	@Path("/{courseId}/feedback")
	public FeedbackResource getFeedbackResource() {
		return feedbackResource;
	}

}
