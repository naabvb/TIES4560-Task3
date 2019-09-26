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
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/clubs")
public class ClubsResource {
    ClubsService clubsService = new ClubsService();
    
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
    public List<Club> getClubs() {
        return clubsService.getAllClubs();
    }
    
    @GET
    @Path("/{clubId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Club getClub(@PathParam("clubId") long id) {
        Club club = clubsService.getClub(id);
        return club;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Club addClub(Club club) {
        return clubsService.addClub(club);
    }
    
    @PUT
    @Path("/{clubId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Club updateClub(@PathParam("clubId") long id, Club club) {
        club.setId(id); // TODO: Is this really correct?
        return clubsService.updateClub(club);
    }
    
    @DELETE
    @Path("/{clubId}")
    public void deleteClub(@PathParam("clubId") long id) {
        clubsService.removeClub(id);
    }
    
    // TODO: getActivitiesResource?
}
