package CourseClub.register;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course {

	private long id;
	private String title;
	private String teacher;
	private List<Link> links = new ArrayList<Link>();

	public Course() {
		// stub
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

	public void setId(long id) {
		this.id = id;

	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getTitle() {
		return title;
	}

	public long getId() {
		return id;
	}

	public boolean hasRequiredAttributes() {
		return ((this.getTitle() != null && !this.getTitle().isEmpty()) && this.getTeacher() != null
				&& !this.getTeacher().isEmpty());
	}
}
