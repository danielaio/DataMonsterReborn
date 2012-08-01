import java.util.ArrayList;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class FollowersCollector {

	FollowersStorageUtils utils;

	public FollowersCollector (String coll) {
		this.utils = new FollowersStorageUtils(coll);
	}

	public void collectAllFollowers(String[] args, int iters) {

		Twitter twitter = new TwitterFactory(Authentication.authenticate()).getInstance();

		long cursor = -1;
		IDs ids;

		ArrayList<Long> followers = new ArrayList<Long>();

		for (String one : args) {
			try {
				int count = 0;
				do {
					ids = twitter.getFollowersIDs(one, cursor);
					for (long id : ids.getIDs()) {
						followers.add(id);
					}
					utils.storeFollowers(followers);
					count++;

				} while ((cursor = ids.getNextCursor()) != 0 && count < iters);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	//could add some sort of randomisation here...
	public long[] getCollectedFollowers() {
		return utils.getCollectedFollowersFromDB();
	}
	
}
