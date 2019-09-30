package CourseClub.register;


import CourseClub.register.Exceptions.ResourceNotFoundException;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Singleton
@Path("/clubs")
public class ClubsResource {
    ClubsService clubsService = new ClubsService();
	ActivitiesResource activitiesResource = new ActivitiesResource();
    
    // This seems to break methods. Probably because there are multiple @GET-annotated methods
    /*
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String asdf() {
        // TODO: rename or remove
        // TODO: Return json or xml?
        return "here be clubs";
    }
    */
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Club> getClubs() throws NoContentException {
        List<Club> clubs =  clubsService.getAllClubs();
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
        club.setId(id); // TODO: Is this really correct?
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
