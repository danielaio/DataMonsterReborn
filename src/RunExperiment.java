
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

		//collectYoung();
		collectOld();
	}

	public static void collectYoung() {
		String youngTweetsColl = "youngstersTweets";
		String youngFollowersColl = "youngFollowers";
		String[] youngCelebrities = {"GLEEonFOX", 
				"carlyraejepsen", "RyanSeacrest", "MTV", "RIDEBMX",
				"EA", "PlayStation", "J14Magazine", "twilight_fan",
				"MTVteenwolf", "IMDb", "gossipgirl", "youngdemocrat", 
				"younglibs", "BCYoungLiberals", "wayoungliberals", "taylorswift13", "justinbieber"};
		String featuresFile = "youngFeaturesNaiveBayes.txt";
		String label = "Y";
		String in = "forTagging.txt";
		String out = "output.txt";

		collect(youngCelebrities, youngFollowersColl, youngTweetsColl, in, out, featuresFile, label);
	}

	public static void collectOld() {
		

		String oldTweetsColl = "oldstersTweets";
		String oldFollowersColl = "oldFollowers";
		String[] oldCelebrities = {"AP", "MarthaStewart", "Oprah", "USAgov", "IBDinvestors", 
				"Emeril", "hedgefundinvest", "BreakoutStocks", "NS_ukgov", "wallstCS",
				"WSJ", "beegeesforever", "PaulMcCartney", "eltonjohndotcom",
				"MickJagger","RatPack_Frank", "BarbaraJWalters", "WholeFoods", "FinancialTimes", "BarackObama"};
		String featuresFile = "oldFeaturesNaiveBayes.txt";
		String label = "O";
		String in = "forTaggingOld.txt";
		String out = "outputOld.txt";

		collect(oldCelebrities, oldFollowersColl, oldTweetsColl, in, out, featuresFile, label);

	}
	
	
	/**
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
	public static void collect(String[] celebs, String followerColl, String tweetsColl, String fileToTag, String taggedFile, String featuresFile, String label) {


		//listen for tweets
//		FollowersCollector collector = new FollowersCollector(followerColl);
//		collector.collectAllFollowers(celebs, 1);
//
//		long[] collectedFollowers = collector.getCollectedFollowers();
//
		TweetCollector tweetCollector = new TweetCollector(tweetsColl);
//
//		for (int i = 0; i < 7; i++) {
//			long[] notAll = Arrays.copyOfRange(collectedFollowers, i*5000, (i+1)*5000);
//			tweetCollector.getStream(notAll, 600000);
//		}
//
//		//tag the tweets
//		tweetCollector.createFileForTagging(fileToTag);
//		TaggerUtils.runPOSTagger(fileToTag, taggedFile);
//		System.out.println("The pos tagger is finished.");

		tweetCollector.storeTaggedTweetsForNaiveBayes(taggedFile);
		
		//extract features
		FeatureExtraction extr = new FeatureExtraction(featuresFile, label, tweetsColl);
	//	extr.extractFeatures();
		extr.extractFeaturesNaiveBayesPerTweet();
	}
}
