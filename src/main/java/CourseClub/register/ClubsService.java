package CourseClub.register;

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
        return null; // TODO: Throw an exception instead?
    }

    public Club addClub(Club club) {
        club.setId(nextId);
        nextId++;
        clubs.add(club);
        return club;
    }

    public Club updateClub(Club club) {
        int clubIndex = findClubIndex(club.getId());
        if (clubIndex >= 0) {
            clubs.set(clubIndex, club);
            return club;
        }
        return null; // TODO: throw an exception
    }

    public void removeClub(long id) {
        int clubIndex = findClubIndex(id);
        if (clubIndex >= 0) {
            clubs.remove(clubIndex);
            return;
        }
        // TODO: Throw an exception
    }
    
    private int findClubIndex(long id) {
        for (int i = 0; i < clubs.size(); i++) {
            if (clubs.get(i).getId() == id) {
                return i;
            }
        }
        return -1; // TODO: Throw an exception instead?
    }
}
