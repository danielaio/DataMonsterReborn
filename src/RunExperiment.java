
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


public final class RunExperiment {
	/**
	 * Usage: java twitter4j.examples.friendsandfollowers.GetFollowersIDs [screen name]
	 *
	 * @param args message
	 */

	public static void main(String[] args) {


		String youngTweetsColl = "youngstersTweets";
		String youngFollowersColl = "youngFollowers";
		String[] youngCelebrities = {"GLEEonFOX", 
				"carlyraejepsen", "RyanSeacrest", "MTV", "RIDEBMX",
				"EA", "PlayStation", "J14Magazine", "twilight_fan",
				"MTVteenwolf", "IMDb", "gossipgirl", "youngdemocrat", 
				"younglibs", "BCYoungLiberals", "wayoungliberals", "taylorswift13", "justinbieber"};

		//String oldFollowersColl = "oldTweets";
		//String[] oldCelebrities = {};

		collect(youngCelebrities, youngFollowersColl, youngTweetsColl);

	}

	/*
	 * Start by extracting all the followers of the celebrities.
	 * Insert the followers in the respective collection.
	 * From the collection, get all the followers. Then
	 * listen for the stream, having taken some random sample
	 * of the followers, as not all of them could fit into a file.
	 * getStream stores the tweets in the respective collection.
	 * 
	 * Repeat for the negative data set.
	 * 
	 */


	public static void collect(String[] celebs, String followerColl, String tweetsColl) {
//
//		FollowersCollector collector = new FollowersCollector(followerColl);
//		collector.collectAllFollowers(celebs, 1);
//
//		long[] collectedFollowers = collector.getCollectedFollowers();
//
		TweetCollector tweetColl = new TweetCollector(tweetsColl);
//
//		for (int i = 0; i < 7; i++) {
//			long[] notAll = Arrays.copyOfRange(collectedFollowers, i*5000, (i+1)*5000);
//			tweetColl.getStream(notAll, 120000);
//		}

		String in = "forTagging.txt";
		String out = "output.txt";
//		tweetColl.createFileForTagging(in);
//		TaggerUtils.runPOSTagger(in, out);
//		tweetColl.storeTaggedTweets(out);

//		tweetColl.getAllTweets();
		
		String featuresFile = "youngUsers.txt";
		String label = "Y";
		FeatureExtraction extr = new FeatureExtraction(featuresFile, label, tweetsColl);
		extr.extractFeatures();
	}
}
