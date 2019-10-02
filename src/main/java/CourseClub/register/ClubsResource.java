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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import CourseClub.register.Exceptions.ResourceNotFoundException;
import CourseClub.register.Services.ClubsService;
import CourseClub.register.Types.Club;

@Singleton
@Path("/clubs")
public class ClubsResource {
	ClubsService clubsService = new ClubsService();
	ActivitiesResource activitiesResource = new ActivitiesResource();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Club> getClubs() throws NoContentException {
		List<Club> clubs = clubsService.getAllClubs();
		if (!clubs.isEmpty()) {
			return clubs;
		} else {
			throw new NoContentException("No courses found.");
		}
	}

	@GET
	@Path("/{clubId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Club getClub(@PathParam("clubId") long id) {
		Club club = clubsService.getClub(id);
		if (club == null) {
			throw new ResourceNotFoundException("Club with id " + id + " not found.");
		}
		return club;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addClub(Club club, @Context UriInfo uriInfo) {
		Club newClub = clubsService.addClub(club);
		String uri = uriInfo.getBaseUriBuilder().path(ClubsResource.class).path(Long.toString(club.getId())).build()
				.toString();
		newClub.addLink(uri, "self");

		uri = uriInfo.getBaseUriBuilder().path(ClubsResource.class).path(ClubsResource.class, "getActivitiesResource")
				.resolveTemplate("clubId", club.getId()).build().toString();
		newClub.addLink(uri, "activities");

		String newId = String.valueOf(newClub.getId());
		URI url = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(url).entity(newClub).build();
	}

	@PUT
	@Path("/{clubId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Club updateClub(@PathParam("clubId") long id, Club club) {
		club.setId(id);
		return clubsService.updateClub(club);
	}

	@DELETE
	@Path("/{clubId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteClub(@PathParam("clubId") long id) {
		return clubsService.removeClub(id);
	}

	@Path("/{clubId}/activities")
	public ActivitiesResource getActivitiesResource() {
		return activitiesResource;
	}

}
