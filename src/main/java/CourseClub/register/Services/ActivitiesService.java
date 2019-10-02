package CourseClub.register.Services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import CourseClub.register.Exceptions.BadRequestException;
import CourseClub.register.Exceptions.ResourceNotFoundException;
import CourseClub.register.Types.Activity;
import CourseClub.register.Types.Link;

@Singleton
public class ActivitiesService {

	private List<Activity> activities;
	private static long nextId = 0L;

	public ActivitiesService() {
		this.activities = new ArrayList<Activity>();
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public static long getNextId() {
		return nextId;
	}

	public static void setNextId(long nextId) {
		ActivitiesService.nextId = nextId;
	}

	public List<Activity> getActivitiesFromClub(long clubId) {
		List<Activity> filtered = new ArrayList<Activity>();
		for (int i = 0; i < activities.size(); i++) {
			if (activities.get(i).getClubId() == clubId) {
				filtered.add(activities.get(i));
			}
		}
		return filtered;
	}

	public Activity getActivity(long clubId, long activityId) {
		for (int i = 0; i < activities.size(); i++) {
			if (activities.get(i).getId() == activityId && activities.get(i).getClubId() == clubId)
				return activities.get(i);
		}
		return null;
	}

	public Activity addActivity(Activity activity, long clubId) {
		if (activity.hasRequiredAttributes()) {
			activity.setId(nextId);
			activity.setClubId(clubId);
			nextId++;
			activities.add(activity);
			return activity;
		} else {
			throw new BadRequestException("Couldn't add activity; missing required attributes.");
		}
	}

	public Activity updateActivity(Activity activity) {
		int index = findActivityIndex(activity.getId());
		if (index >= 0) {
			if (activity.hasRequiredAttributes()) {
				List<Link> links = activities.get(index).getLinks();
				activity.setLinks(links);
				activities.set(index, activity);
				return activity;
			} else {
				throw new BadRequestException("Couldn't update activity; missing required attributes.");
			}
		} else {
			throw new ResourceNotFoundException("Couldn't find activity with id " + activity.getId());
		}
	}

	private int findActivityIndex(long id) {
		for (int i = 0; i < activities.size(); i++) {
			if (activities.get(i).getId() == id)
				return i;
		}
		return -1;
	}

	public Response removeActivity(long id) {
		int index = findActivityIndex(id);
		if (index >= 0) {
			activities.remove(index);
			return Response.status(204).build();
		} else {
			throw new ResourceNotFoundException("Couldn't find activity with id " + id);
		}
	}

}
