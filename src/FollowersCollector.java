import java.util.ArrayList;

import twitter4j.IDs;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;

import com.mongodb.DBCursor;

public class FollowersCollector {

	FollowersStorageUtils utils;
	private Configuration conf;
	
	public FollowersCollector (String coll, final Configuration conf) {
		this.utils = new FollowersStorageUtils(coll);
		this.conf = conf;
	}

	public void collectAllFollowers(String[] args, int iters) {

		Twitter twitter = new TwitterFactory(conf).getInstance();
		IDs ids;

		for (String one : args) {
			ArrayList<Long> followers = new ArrayList<Long>();
			try {
				int count = 0;
				long cursor = -1;
				do {
					ids = twitter.getFollowersIDs(one, cursor);
					RateLimitStatus status = ids.getRateLimitStatus();
					for (long id : ids.getIDs()) {
						followers.add(id);
					}
					utils.storeFollowers(followers);
					count++;
					
					if (status.getRemainingHits() < 10) {
						try {
							int secondsUntilReset = status.getSecondsUntilReset() > 0 ? status.getSecondsUntilReset() : 1;
							Thread.sleep(secondsUntilReset * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				
				} while ((cursor = ids.getNextCursor()) != 0 && count < iters);
				System.out.println(followers.size() + " " + one);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	//could add some sort of randomisation here...
	public long[] getCollectedFollowers() {
		return utils.getCollectedFollowersFromDB();
	}
	
	public DBCursor getCollectedFollowersFromDBInPortions(int max) {
		return utils.getCollectedFollowersFromDBInPortions(max);
	}
	public long[] getRandomSampleOfFollowers(int number) {
		return utils.getRandomSampleOfFollowersFromDB(number);
	}
	
}
