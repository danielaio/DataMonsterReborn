import java.util.ArrayList;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class FollowersCollector {

	DatabaseUtils utils;

	public FollowersCollector (DatabaseUtils utils) {
		this.utils = utils;
	}

	public void getAllFollowers(String[] args, int iters) {

		Twitter twitter = new TwitterFactory(TweetCollector.authenticate()).getInstance();

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

	private static long[] getFollowers(String[] args) {

		try {

			Twitter twitter = new TwitterFactory().getInstance();
			long cursor = -1;
			IDs ids;
			System.out.println("Listing followers's ids.");

			ArrayList<Long> followers = new ArrayList<Long>();

			int count = 1;
			do {
				if (0 < args.length) {
					ids = twitter.getFollowersIDs(args[0], cursor);
				} else {
					ids = twitter.getFollowersIDs(cursor);
				}
				for (long id : ids.getIDs()) {
					count++;
					followers.add(id);
				}
			} while ((cursor = ids.getNextCursor()) != 0 && count <= 1000);

			long[] followArray = new long[followers.size()];
			for(int i = 0; i < followers.size(); i++) {
				followArray[i] = followers.get(i);
			}


			//	System.out.println(count);

			return followArray;

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get followers' ids: " + te.getMessage());

			return null;
		}
	}

}
