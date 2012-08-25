import java.util.ArrayList;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;


public class FollowersCollector {

	FollowersStorageUtils utils;
	private Configuration conf;
	
	public FollowersCollector (String coll, final Configuration conf) {
		this.utils = new FollowersStorageUtils(coll);
		this.conf = conf;
	}

	public void collectAllFollowers(String[] args, int iters) {

		Twitter twitter = new TwitterFactory(conf).getInstance();

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
