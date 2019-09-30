package CourseClub.register;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Club {
    private long id;
    private String name;
    private List<Activity> activities;
    private List<Link> links = new ArrayList<Link>();
    
    public Club() {
        //
    }

    public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLink(String url, String rel) {
		Link link = new Link();
		link.setLink(url);
		link.setRel(rel);
		links.add(link);
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

    public boolean hasRequiredAttributes() {
        return (this.getName() != null && !this.getName().isEmpty());
    }

    // TODO: add operations for editing activities?
}
