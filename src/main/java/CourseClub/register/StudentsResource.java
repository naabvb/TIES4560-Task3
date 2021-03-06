package CourseClub.register;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import CourseClub.register.Exceptions.ResourceNotFoundException;
import CourseClub.register.Services.StudentsService;
import CourseClub.register.Types.Student;

@Path("/students")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentsResource {

	private StudentsService studentsService = new StudentsService();

	@GET
	@PermitAll
	public List<Student> getStudents(@PathParam("courseId") long courseId) {
		return studentsService.getStudentsFromCourse(courseId);
	}

	@POST
	@RolesAllowed({ "user", "admin" })
	public Response addStudent(@PathParam("courseId") long courseId, Student student, @Context UriInfo uriInfo) {
		Student newStudent = studentsService.addStudent(student, courseId);
		String uri = uriInfo.getBaseUriBuilder().path(CoursesResource.class).path(Long.toString(courseId))
				.path(StudentsResource.class).path(Long.toString(student.getId())).build().toString();
		newStudent.addLink(uri, "self");

		uri = uriInfo.getBaseUriBuilder().path(CoursesResource.class).path(Long.toString(courseId)).build().toString();
		newStudent.addLink(uri, "course");

		String newId = String.valueOf(newStudent.getId());
		URI url = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(url).entity(newStudent).build();
	}

	@GET
	@Path("/{studentId}")
	@PermitAll
	public Student getStudent(@PathParam("courseId") long courseId, @PathParam("studentId") long studentId) {
		Student student = studentsService.getStudent(courseId, studentId);
		if (student == null) {
			throw new ResourceNotFoundException(
					"Student with id " + studentId + " on course " + courseId + " not found");
		}
		return student;
	}

	@PUT
	@Path("/{studentId}")
	@RolesAllowed({ "user", "admin" })
	public Student updateStudent(@PathParam("studentId") long studentId, Student student) {
		student.setId(studentId);
		return studentsService.updateStudent(student);
	}

	@DELETE
	@Path("/{studentId}")
	@RolesAllowed("admin")
	public Response deleteStudent(@PathParam("studentId") long studentId) {
		return studentsService.removeStudent(studentId);
	}
}
