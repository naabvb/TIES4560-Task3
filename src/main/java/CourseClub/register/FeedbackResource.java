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
import CourseClub.register.Services.FeedbackService;
import CourseClub.register.Types.Feedback;

@Path("/feedback")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackResource {

	private FeedbackService feedbackService = new FeedbackService();

	@GET
	@PermitAll
	public List<Feedback> getFeedback(@PathParam("courseId") long courseId) {
		return feedbackService.getFeedbackFromCourse(courseId);
	}

	@POST
	@RolesAllowed({ "user", "admin" })
	public Response addFeedback(@PathParam("courseId") long courseId, Feedback feedback, @Context UriInfo uriInfo) {
		Feedback newFeedback = feedbackService.addFeedback(feedback, courseId);
		String uri = uriInfo.getBaseUriBuilder().path(CoursesResource.class).path(Long.toString(courseId))
				.path(FeedbackResource.class).path(Long.toString(feedback.getId())).build().toString();
		newFeedback.addLink(uri, "self");

		uri = uriInfo.getBaseUriBuilder().path(CoursesResource.class).path(Long.toString(courseId)).build().toString();
		newFeedback.addLink(uri, "course");

		String newId = String.valueOf(newFeedback.getId());
		URI url = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(url).entity(newFeedback).build();
	}

	@GET
	@Path("/{feedbackId}")
	@PermitAll
	public Feedback getFeedback(@PathParam("courseId") long courseId, @PathParam("feedbackId") long feedbackId) {
		Feedback feedback = feedbackService.getFeedback(courseId, feedbackId);
		if (feedback == null) {
			throw new ResourceNotFoundException(
					"Feedback with id " + feedbackId + " on course " + courseId + " not found.");
		}
		return feedback;
	}

	@PUT
	@Path("/{feedbackId}")
	@RolesAllowed({ "user", "admin" })
	public Feedback updateFeedback(@PathParam("feedbackId") long feedbackId, Feedback feedback) {
		feedback.setId(feedbackId);
		return feedbackService.updateFeedback(feedback);
	}

	@DELETE
	@Path("/{feedbackId}")
	@RolesAllowed("admin")
	public Response deleteFeedback(@PathParam("feedbackId") long feedbackId) {
		return feedbackService.removeFeedback(feedbackId);
	}
}
