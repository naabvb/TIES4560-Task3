package CourseClub.register.Types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Student {
	private String name;
	private long id;
	private long onCourseId;
	private List<Link> links = new ArrayList<Link>();

	public long getOnCourseId() {
		return onCourseId;
	}

	public void setOnCourseId(long onCourseId) {
		this.onCourseId = onCourseId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Student() {
		// Stub
	}

	public boolean hasRequiredAttributes() {
		return (this.getName() != null && !this.getName().isEmpty());
	}
}
