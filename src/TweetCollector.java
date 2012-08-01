import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;


public class TweetCollector {

	TweetsStorageUtils utils;
	
	public TweetCollector(String coll) {
		utils = new TweetsStorageUtils(coll);
	}
	
	public void createFileForTagging(String filename) {
		utils.createFileForTagging(filename);
	}
	
	public void storeTaggedTweets(String output) {
		utils.storeTaggedTweets(output);
	}
	
	public void getAllTweets() {
		utils.getAllTweets();
	}
	
	public void getStream(long[] followArray, long milliseconds) {

		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {

				String rawJSON = DataObjectFactory.getRawJSON(status);
				utils.storeTweet(rawJSON);
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		TwitterStream twitterStream = new TwitterStreamFactory(Authentication.authenticate()).getInstance();
		twitterStream.addListener(listener);

		System.out.println("Printing beliebers' tweets :):)");
		twitterStream.filter(new FilterQuery(0, followArray, null));

		try {
			Thread.sleep(milliseconds);
			twitterStream.cleanUp();
			twitterStream.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
