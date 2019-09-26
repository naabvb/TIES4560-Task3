package CourseClub.register;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Club {
    private long id;
    private String name;
    private List<Activity> activities;
    
    public Club() {
        //
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    // TODO: add operations for editing activities?
}
