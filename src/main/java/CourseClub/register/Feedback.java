package CourseClub.register;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Feedback {
	private String feedbackText;
	private long id;
	private long onCourseId;
	private String feedbackSender;
	private List<Link> links = new ArrayList<Link>();

	public Feedback() {
		// stub
	}

	public void addLink(String url, String rel) {
		Link link = new Link();
		link.setLink(url);
		link.setRel(rel);
		links.add(link);
	}

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOnCourseId() {
		return onCourseId;
	}

	public void setOnCourseId(long onCourseId) {
		this.onCourseId = onCourseId;
	}

	public String getFeedbackSender() {
		return feedbackSender;
	}

	public void setFeedbackSender(String feedbackSender) {
		this.feedbackSender = feedbackSender;
	}

	public boolean hasRequiredAttributes() {
		return ((this.getFeedbackSender() != null && !this.getFeedbackSender().isEmpty())
                && (this.getFeedbackText() != null && !this.getFeedbackSender().isEmpty()));
	}

}
