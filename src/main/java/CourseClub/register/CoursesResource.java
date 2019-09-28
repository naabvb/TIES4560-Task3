package CourseClub.register;

import java.net.URI;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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

	@GET
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course getCourse(@PathParam("courseId") long id) {
		Course course = coursesService.getCourse(id);
		return course;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCourse(Course course, @Context UriInfo uriInfo) {
		Course newCourse = coursesService.addCourse(course);
		String uri = uriInfo.getBaseUriBuilder().path(CoursesResource.class).path(Long.toString(course.getId())).build()
				.toString();
		newCourse.addLink(uri, "self");

		uri = uriInfo.getBaseUriBuilder().path(CoursesResource.class).path(CoursesResource.class, "getStudentsResource")
				.resolveTemplate("courseId", course.getId()).build().toString();
		newCourse.addLink(uri, "students");
		String newId = String.valueOf(newCourse.getId());
		URI url = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(url).entity(newCourse).build();

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
