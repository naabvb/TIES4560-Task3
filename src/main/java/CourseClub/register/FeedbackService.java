package CourseClub.register;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import CourseClub.register.Exceptions.BadRequestException;
import CourseClub.register.Exceptions.ResourceNotFoundException;

@Singleton
public class FeedbackService {

	private List<Feedback> feedbacks;
	private static long nextId = 0L;

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public static long getNextId() {
		return nextId;
	}

	public static void setNextId(long nextId) {
		FeedbackService.nextId = nextId;
	}

	public FeedbackService() {
		this.feedbacks = new ArrayList<Feedback>();
	}

	public List<Feedback> getFeedbackFromCourse(long courseId) {
		List<Feedback> filtered = new ArrayList<Feedback>();
		for (int i = 0; i < feedbacks.size(); i++) {
			if (feedbacks.get(i).getOnCourseId() == courseId) {
				filtered.add(feedbacks.get(i));
			}
		}
		return filtered;
	}

	public Feedback addFeedback(Feedback feedback, long courseId) {
		if (feedback.hasRequiredAttributes()) {
			feedback.setId(nextId);
			feedback.setOnCourseId(courseId);
			nextId++;
			feedbacks.add(feedback);
			return feedback;
		} else {
			throw new BadRequestException("Couldn't add feedback; missing required attributes.");
		}
	}

	public Feedback getFeedback(long courseId, long feedbackId) {
		for (int i = 0; i < feedbacks.size(); i++) {
			if (feedbacks.get(i).getId() == feedbackId && feedbacks.get(i).getOnCourseId() == courseId)
				return feedbacks.get(i);
		}
		return null;
	}

	public Feedback updateFeedback(Feedback feedback) {
		int index = findFeedbackIndex(feedback.getId());
		if (index >= 0) {
			if (feedback.hasRequiredAttributes()) {
				List<Link> links = feedbacks.get(index).getLinks();
				feedback.setLinks(links);
				feedbacks.set(index, feedback);
				return feedback;
			} else {
				throw new BadRequestException("Couldn't update feedback; missing required attributes.");
			}
		} else {
			throw new ResourceNotFoundException("Couldn't find feedback with id " + feedback.getId());
		}
	}

	public Response removeFeedback(long feedbackId) {
		int index = findFeedbackIndex(feedbackId);
		if (index >= 0) {
			feedbacks.remove(index);
			return Response.status(204).build();
		} else {
			throw new ResourceNotFoundException("Couldn't find feedback with id " + feedbackId);
		}
	}

	private int findFeedbackIndex(long id) {
		for (int i = 0; i < feedbacks.size(); i++) {
			if (feedbacks.get(i).getId() == id)
				return i;
		}
		return -1;
	}

}
