import java.util.ArrayList;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.RateLimitStatus;

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

		ArrayList<Long> followers;
		
		RateLimitStatus status;

		for (String one : args) {
			followers = new ArrayList<Long>();
			try {
				int count = 0;
				do {
					ids = twitter.getFollowersIDs(one, cursor);
					status = ids.getRateLimitStatus();
					for (long id : ids.getIDs()) {
						followers.add(id);
					}
					utils.storeFollowers(followers);
					count++;
					
					if (status.getRemainingHits() < 5) {
						try {
							Thread.sleep(status.getSecondsUntilReset());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				
				} while ((cursor = ids.getNextCursor()) != 0 && count < iters);
				System.out.println(followers.size());
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
