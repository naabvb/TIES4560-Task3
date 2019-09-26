package CourseClub.register;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Activity {
    private long id;
    private String name;
    
    public Activity() {
        // stub
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

}
