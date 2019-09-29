package CourseClub.register;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

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
		feedback.setId(nextId);
		feedback.setOnCourseId(courseId); // TODO PUUTTUVAT EXCP
		nextId++;
		feedbacks.add(feedback);
		return feedback;
	}

	public Feedback getFeedback(long courseId, long feedbackId) {
		for (int i = 0; i < feedbacks.size(); i++) {
			if (feedbacks.get(i).getId() == feedbackId && feedbacks.get(i).getOnCourseId() == courseId)
				return feedbacks.get(i);
		}
		return null; // EXP
	}

	public Feedback updateFeedback(Feedback feedback) {
		int index = findFeedbackIndex(feedback.getId());
		if (index >= 0) {
			feedbacks.set(index, feedback);
			return feedback;
		}
		return null; // eXP
	}

	public void removeFeedback(long feedbackId) {
		int index = findFeedbackIndex(feedbackId);
		if (index >= 0) {
			feedbacks.remove(index);
		}

	}

	private int findFeedbackIndex(long id) {
		for (int i = 0; i < feedbacks.size(); i++) {
			if (feedbacks.get(i).getId() == id)
				return i;
		}
		return -1; // TODO EXCEPTION
	}

}
