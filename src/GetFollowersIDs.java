
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.mongodb.DBCursor;

import edu.cmu.cs.lti.ark.tweetnlp.RunPOSTagger;

import twitter4j.FilterQuery;
import twitter4j.IDs;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
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


		DatabaseUtils utils = new DatabaseUtils();
		
		FollowersCollector collector = new FollowersCollector(utils);
		//collector.getAllFollowers(args, 1);
		long[] collectedFollowersFromDB = utils.getCollectedFollowersFromDB();
		long[] notAll = Arrays.copyOfRange(collectedFollowersFromDB, 5000, 10000);
		
		TweetCollector.getStream(notAll, 1800000);

		//		String in = "forTagging.txt";
		//		String out = "output.txt";


		//		DatabaseUtils utils = new DatabaseUtils();
		//		utils.createFileForTagging(in);

		//		TaggerUtils.runPOSTagger(in, out);
		//		utils.storeTaggedTweets(out);


		//		utils.getAllTweets();

		//		System.out.println(utils.getUsers().toString());
		//		System.out.println(utils.getUsers().get(0));
		//		DBCursor tweetsForUser = utils.getTweetsForUser(utils.getUsers().get(0));
		//		while (tweetsForUser.hasNext()) {
		//			System.out.println(tweetsForUser.next());
		//		}

		//		FeatureExtraction extr = new FeatureExtraction("youngUsers.txt", "Y");
		//		extr.extractFeatures();

	}

}
