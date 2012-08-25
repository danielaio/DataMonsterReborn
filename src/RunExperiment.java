
import java.util.Arrays;

import twitter4j.conf.Configuration;


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
		String[] youngCelebrities = {"GLEEonFOX", 
				"carlyraejepsen", "RyanSeacrest", "MTV", "RIDEBMX",
				"EA", "PlayStation", "J14Magazine", "twilight_fan",
				"MTVteenwolf", "IMDb", "gossipgirl", "youngdemocrat", 
				"younglibs", "BCYoungLiberals", "wayoungliberals", "taylorswift13", "justinbieber",
				"Eminem", "britneyspears", "MileyCyrus", "TheRock",
				"aplusk", "Chegg", "TheDerekJohnson", "winscholarships", "USATODAYcollege",
				"mtvU", "CollegeDJ", "MyCollegeOnline", "Harvard", "Harvard_Law", "Stanford",
				"Buckeye_Nation", "WUSTLsoccer", "ArkRazorbacks", "SRCollegeSports", "SRCollegeSports",
				"CFBONFOX", "HawaiiAthletics", "DailyEmerald", "JMUSports", "ThePittNews",
				"katyperry", "rihanna", "NICKIMINAJ", "SnoopDogg"};
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
				"MickJagger","RatPack_Frank", "BarbaraJWalters", "WholeFoods", "FinancialTimes", "BarackObama"
				 ,"DAVID_LYNCH", "algore", "andersoncooper", "TheDemocrats", "thinkprogress", "indecision", "Schwarzenegger",
				 "rollcall", "iRevolt", "JeffreyFeldman", "DailyCaller", "DWStweets", "TheWeek",
				 "BrainLine", "summertomato", "KelliThompson", "kellyoxford", "organicdealsmom"
				 , "Babyjobamboo", "LizSzabo", "cheeriokeeper", "MommyNews", "Nightowlmama",
				 "Mother_Tongue", "babygoodbuys", "SarahMaizes", "TheChefsWife", "VictoryTrue",
				 "bookpubs", "LitChat", "PenguinUKBooks", "littlebrown", "books", "MaryAnnScheuer",
				 "smashingmag", "webdesignledger", "chrisspooner", "BloombergNews", "dantanner",
				 "tomkeene", "SmarTrend", "Reuters", "jessefelder", "tnewbold", "parenting",
				 "YourChessCoach", "adnys", "timdub", "RichHopkins", "n2nbroadway", "n2nbroadway"
				 , "travisbedard", "espn", "nfl", "MariahCarey", "davidguetta"};
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
		FollowersCollector collector = new FollowersCollector(followerColl, conf);
		collector.collectAllFollowers(celebs, 1000);

		long[] collectedFollowers = collector.getCollectedFollowers();

		TweetCollector tweetCollector = new TweetCollector(tweetsColl, conf);

//		for (int i = 0; i < 7; i++) {
//			long[] notAll = Arrays.copyOfRange(collectedFollowers, i*5000, (i+1)*5000);
//			tweetCollector.getStream(notAll, 600000);
//		}

//		//tag the tweets
//		tweetCollector.createFileForTagging(fileToTag);
//		TaggerUtils.runPOSTagger(fileToTag, taggedFile);
//		System.out.println("The pos tagger is finished.");

//		tweetCollector.storeTaggedTweetsForNaiveBayes(taggedFile);
		
		//extract features
//		FeatureExtraction extr = new FeatureExtraction(featuresFile, label, tweetsColl);
	//	extr.extractFeatures();
//		extr.extractFeaturesNaiveBayesPerTweet();
	}
}
