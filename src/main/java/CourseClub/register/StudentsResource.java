package CourseClub.register;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentsResource {

	private StudentsService studentsService = new StudentsService();

	@GET
	public List<Student> getStudents(@PathParam("courseId") long courseId) {
		return studentsService.getAllStudents(courseId);
	}

	@GET
	@Path("/{studentId}")
	public Student getStudent(@PathParam("courseId") long courseId, @PathParam("studentId") long studentId) {
		return studentsService.getStudent(courseId, studentId);
	}
}
