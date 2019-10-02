package CourseClub.register.Services;

import CourseClub.register.Exceptions.BadRequestException;
import CourseClub.register.Exceptions.ResourceNotFoundException;
import CourseClub.register.Types.Club;
import CourseClub.register.Types.Link;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class ClubsService {
    long nextId = 0L;
    List<Club> clubs = new ArrayList<Club>();
    
    public List<Club> getAllClubs() {
        return clubs;
    }

    public Club getClub(long id) {
        int clubIndex = findClubIndex(id);
        if (clubIndex >= 0) {
            return clubs.get(clubIndex);
        }
        return null;
    }

    public Club addClub(Club club) {
        if (club.hasRequiredAttributes()) {
            club.setId(nextId);
            nextId++;
            clubs.add(club);
            return club;
        } else {
            throw new BadRequestException("Couldn't add club; missing required attributes.");
        }
    }

    public Club updateClub(Club club) {
        int clubIndex = findClubIndex(club.getId());
        if (clubIndex >= 0) {
            if (club.hasRequiredAttributes()) {
                List<Link> links = clubs.get(clubIndex).getLinks();
                club.setLinks(links);
                clubs.set(clubIndex, club);
                return club;
            } else {
                throw new BadRequestException("Couldn't update club; missing required attributes.");
            }
        } else {
            throw new ResourceNotFoundException("Couldn't find club with id " + club.getId());
        }
    }

    public Response removeClub(long id) {
        int clubIndex = findClubIndex(id);
        if (clubIndex >= 0) {
            clubs.remove(clubIndex);
            return Response.status(204).build();
        } else {
            throw new ResourceNotFoundException("Couldn't find club with id " + id);
        }
    }
    
    private int findClubIndex(long id) {
        for (int i = 0; i < clubs.size(); i++) {
            if (clubs.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
