
import java.util.Arrays;

import twitter4j.conf.Configuration;

import com.mongodb.DBCursor;


public final class RunExperiment {
	/**
	 * Usage: java twitter4j.examples.friendsandfollowers.GetFollowersIDs [screen name]
	 *
	 * @param args message
	 */
	private static Configuration conf = Authentication.authenticate();
	public final static String DB = "mydb2";

	public static void main(String[] args) {	
		
		collectYoung();
		collectOld();
	}

	public static void collectYoung() {
		String youngTweetsColl = "youngstersTweets";
		String youngFollowersColl = "youngFollowers";
		String[] youngCelebrities = {"carlyraejepsen", "RyanSeacrest", "MTV", "RIDEBMX",
				"EA", "PlayStation", "J14Magazine", "twilight_fan",
				"MTVteenwolf", "IMDb", "gossipgirl", "youngdemocrat", 
				"taylorswift13", "justinbieber",
				"Eminem", "britneyspears", "StephenAtHome","mtvU", "CollegeDJ", "MyCollegeOnline", "Harvard", "Harvard_Law", "Stanford",
				"younglibs", "BCYoungLiberals", "wayoungliberals", 
				"Buckeye_Nation", "WUSTLsoccer", "ArkRazorbacks", "SRCollegeSports", "SRCollegeSports",
				"CFBONFOX", "HawaiiAthletics", "DailyEmerald", "JMUSports", "ThePittNews",
				"katyperry", "rihanna", "NICKIMINAJ", "SnoopDogg", "MileyCyrus", "TheRock",
				"aplusk", "Chegg", "TheDerekJohnson", "winscholarships", "USATODAYcollege",
				"GLEEonFOX", 
				};
		String featuresFile = "youngFeaturesNaiveBayes.txt";
		String label = "Y";
		String in = "forTagging.txt";
		String out = "output.txt";

		collect(youngCelebrities, youngFollowersColl, youngTweetsColl, in, out, featuresFile, label);
	}

	public static void collectOld() {

		String oldTweetsColl = "oldstersTweets";
		String oldFollowersColl = "oldFollowers";
		String[] oldies = {"Emeril", "hedgefundinvest", "BreakoutStocks", "NS_ukgov", "wallstCS",
				"WSJ", "beegeesforever", "PaulMcCartney", "eltonjohndotcom",
				"MickJagger","RatPack_Frank", "BarbaraJWalters", "WholeFoods", "FinancialTimes", "BarackObama"};
		String[] oldCelebrities = {"DAVID_LYNCH", "algore", "andersoncooper", "TheDemocrats", "thinkprogress", "indecision", "Schwarzenegger",
				 "rollcall", "iRevolt", "JeffreyFeldman", "DailyCaller", "DWStweets", "TheWeek",
				 "BrainLine", "summertomato", "KelliThompson", "kellyoxford", "organicdealsmom"
				 , "Babyjobamboo",  "VictoryTrue",
				 "bookpubs", "LitChat", "PenguinUKBooks", "littlebrown", "books", "MaryAnnScheuer",
				 "smashingmag", "webdesignledger", "chrisspooner", "BloombergNews", "dantanner",
				 "tomkeene", "SmarTrend", "Reuters", "jessefelder", "tnewbold", "parenting",
				 "YourChessCoach", "adnys", "timdub", "RichHopkins", "n2nbroadway", "n2nbroadway"
				 , "travisbedard", "espn", "nfl", "MariahCarey", "MommyNews","LizSzabo", "cheeriokeeper",  "Nightowlmama",
				 "Mother_Tongue", "babygoodbuys", "SarahMaizes", "TheChefsWife","AP", "MarthaStewart", "Oprah", "USAgov", "IBDinvestors", 
				"Emeril", "hedgefundinvest", "BreakoutStocks", "NS_ukgov", "wallstCS",
				"WSJ", "beegeesforever", "PaulMcCartney", "eltonjohndotcom",
				"MickJagger","RatPack_Frank", "BarbaraJWalters", "WholeFoods", "FinancialTimes", "BarackObama"
				 };
		String featuresFile = "oldFeaturesNaiveBayes.txt";
		String label = "O";
		String in = "forTaggingOld.txt";
		String out = "outputOld.txt";

		//collect(oldCelebrities, oldFollowersColl, oldTweetsColl, in, out, featuresFile, label);
		collect(oldies, oldFollowersColl, oldTweetsColl, in, out, featuresFile, label);
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


		FollowersCollector collector = new FollowersCollector(followerColl, conf);
	
		//collector.collectAllFollowers(celebs, 50);
		
		
		int maxUserIDs = 5000;
		DBCursor followersCur = collector.getCollectedFollowersFromDBInPortions(maxUserIDs);
		TweetCollector tweetCollector = new TweetCollector(tweetsColl, conf);
		
		for (int portion = 0; portion < followersCur.count() / maxUserIDs; portion++) {
			long[] some = new long[maxUserIDs];
			for (int i = 0; i < some.length; i++) {
				some[i] = (Long) followersCur.next().get("_id");
			}
			tweetCollector.getStream(some, 600000);
			System.out.println("Starting portion number " + portion);
		}


//		//tag the tweets
//		tweetCollector.createFileForTagging(fileToTag);
//		TaggerUtils.runPOSTagger(fileToTag, taggedFile);
//		System.out.println("The pos tagger is finished.");
//
//		tweetCollector.storeTaggedTweetsForNaiveBayes(taggedFile);
//		tweetCollector.storeTaggedTweets(taggedFile);
		
		//extract features
		FeatureExtraction extr = new FeatureExtraction(featuresFile, label, tweetsColl);
	//	extr.extractFeatures();
		//extr.extractFeaturesNaiveBayesPerTweet();
	}
}
