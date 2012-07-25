
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.cmu.cs.lti.ark.tweetnlp.RunPOSTagger;

import twitter4j.FilterQuery;
import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;


public final class GetFollowersIDs {
	/**
	 * Usage: java twitter4j.examples.friendsandfollowers.GetFollowersIDs [screen name]
	 *
	 * @param args message
	 */
	
	
	
	public static void main(String[] args) {

		//		GetFollowersIDs.getStream(getFollowers(args), 10000);

		//		long[] me = {216501896};
		//		TweetCollector.getStream(me, 120000);

		String in = "forTagging.txt";
		String out = "output.txt";

		DatabaseUtils utils = new DatabaseUtils();
		//utils.createFileForTagging(in);

		//TaggerUtils.runPOSTagger(in, out);
		utils.storeTaggedTweets(out);
		
		utils.getAllTweets();
		
	}

	
	
	//only takes the first arg, need to change to enable lists of people
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
					System.out.println(id);
				}
			} while ((cursor = ids.getNextCursor()) != 0 && count <= 1000);


			long[] followArray = new long[followers.size()];
			for(int i = 0; i < followers.size(); i++) {
				followArray[i] = followers.get(i);
			}

			return followArray;

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get followers' ids: " + te.getMessage());

			return null;
		}
	}


	
}
