package CourseClub.register;

import CourseClub.register.Exceptions.ResourceNotFoundException;

import java.net.URI;
import java.util.List;

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

@Path("/activities")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ActivitiesResource {

	private ActivitiesService activitiesService = new ActivitiesService();

	@GET
	public List<Activity> getActivities(@PathParam("clubId") long clubId) {
		return activitiesService.getActivitiesFromClub(clubId);
	}

	@POST
	public Response addActivity(@PathParam("clubId") long clubId, Activity activity, @Context UriInfo uriInfo) {
		Activity newActivity = activitiesService.addActivity(activity, clubId);
		String uri = uriInfo.getBaseUriBuilder().path(ClubsResource.class).path(Long.toString(clubId))
				.path(ActivitiesResource.class).path(Long.toString(activity.getId())).build().toString();
		newActivity.addLink(uri, "self");

		uri = uriInfo.getBaseUriBuilder().path(ClubsResource.class).path(Long.toString(clubId)).build().toString();
		newActivity.addLink(uri, "club");

		String newId = String.valueOf(newActivity.getId());
		URI url = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(url).entity(newActivity).build();
	}

	@GET
	@Path("/{activityId}")
	public Activity getActivity(@PathParam("clubId") long clubId, @PathParam("activityId") long activityId) {
		Activity activity =  activitiesService.getActivity(clubId, activityId);
		if (activity == null) {
			throw new ResourceNotFoundException("Activity with id " + activityId + " on club " + clubId + "not found.");
		}
		return activity;
	}

	@PUT
	@Path("/{activityId}")
	public Activity updateActivity(@PathParam("activityId") long activityId, Activity activity) {
		activity.setId(activityId);
		return activitiesService.updateActivity(activity);
	}

	@DELETE
	@Path("/{activityId}")
	public Response deleteActivity(@PathParam("activityId") long activityId) {
		return activitiesService.removeActivity(activityId);
	}
}
